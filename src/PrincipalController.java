import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class PrincipalController implements ActionListener, ListSelectionListener {

    private ArrayList<Alumno> alumnos;
    private DefaultListModel<String> modeloLista;
    private Principal vista;
    private int posicion;

    public PrincipalController(Principal vista){
        this.vista = vista;
        anadirListener(this,this);
        posicion=-1;
        this.alumnos=new ArrayList<>();
        modeloLista=new DefaultListModel<>();
        vista.getListaAlumnos().setModel(modeloLista);
    }

    public void anadirListener(ActionListener alistener, ListSelectionListener llistener){
        vista.getBtEliminar().addActionListener(alistener);
        vista.getBtImportar().addActionListener(alistener);
        vista.getBtExportar().addActionListener(alistener);
        vista.getBtGuardar().addActionListener(alistener);
        vista.getBtNuevo().addActionListener(alistener);
        vista.getListaAlumnos().addListSelectionListener(llistener);
    }

    private void nuevoAlumno(){
        posicion=-1;
        vista.getTfDNI().setText("");
        vista.getTfNombre().setText("");
    }

    private void guardarAlumno(){
        String nombre = vista.getTfNombre().getText();
        String dni= vista.getTfDNI().getText();
        if(!(nombre.isEmpty()||dni.isEmpty())){
            if(posicion<0){
                modeloLista.add(modeloLista.getSize(), dni+": "+nombre);
                Alumno alumno=new Alumno(dni, nombre);
                alumnos.add(alumno);
                posicion=alumnos.size()-1;
                vista.getListaAlumnos().setSelectedIndex(posicion);
            } else {
                modeloLista.set(posicion, dni+": "+nombre);
                Alumno alumno=new Alumno(dni, nombre);
                alumnos.set(posicion, alumno);
            }
        }
    }

    private void eliminarAlumno(){
        if(posicion>=0){
            int option = JOptionPane.showConfirmDialog(null, "Estas seguro?", "Eliminar alumno", JOptionPane.YES_NO_OPTION);
            if(option == JOptionPane.YES_OPTION){
                modeloLista.remove(posicion);
                alumnos.remove(posicion);
                if(posicion>=alumnos.size()){
                    posicion=-1;
                    vista.getTfNombre().setText("");
                    vista.getTfDNI().setText("");
                } else {
                    vista.getListaAlumnos().setSelectedIndex(posicion);
                    Alumno alumno=alumnos.get(posicion);
                    vista.getTfDNI().setText(alumno.getDNI());
                    vista.getTfNombre().setText(alumno.getNombre());
                }
            }
        }
    }

    private void importarAlumnos(){
        JFileChooser fileChooser = new JFileChooser(); // Dialogo para escribir/leer desde un fichero
        fileChooser.setDialogTitle("Desde que archivo vas a importar los datos?"); // Se pone un titulo
        int userSelection = fileChooser.showSaveDialog(vista.getBtImportar()); // Se muestra el diologo y se le pasa el
                                                                               // boton de importar como parametro
        if(userSelection == JFileChooser.APPROVE_OPTION){
            // Ver la extension del archivo
            File fileToSave = fileChooser.getSelectedFile();
            String ruta = fileToSave.getAbsolutePath();
            String[] partes = ruta.split("\\.");
            // Elegir funcion a ejecutar dependiendo de la extension
            if(partes[partes.length-1].equals("txt")){
                leerFicheroTextoPlano(ruta);
            } else if (partes[partes.length-1].equals("xml")){
                leerFicheroXML(ruta);
            } else {
                deserializarAlumnos(ruta);
            }
        }
    }

    private void exportarAlumnos(){
        if(!alumnos.isEmpty()){
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Especifica a que archivo vas a esportar los datos");
            int userSelection = fileChooser.showSaveDialog(vista.getBtExportar());
            if (userSelection == JFileChooser.APPROVE_OPTION){
                // Ver la extension del archivo
                File fileToSave = fileChooser.getSelectedFile();
                String ruta = fileToSave.getAbsolutePath();
                String[] partes = ruta.split("\\.");
                // Elegir funcion a ejecutar dependiendo de la extension
                if(partes[partes.length-1].equals("txt")){
                    escribirFicheroTextoPlano(ruta);
                } else if (partes[partes.length-1].equals("xml")){
                    escribirFicheroXML(ruta);
                } else {
                    serializarAlumnos(ruta);
                }
            }
        }
    }



    public void leerFicheroTextoPlano(String ruta) {
        alumnos.clear();
        modeloLista.clear();
        File fichero = null;
        FileReader lector = null;
        BufferedReader buffer = null;

        try {
            buffer = new BufferedReader(new FileReader(new File(ruta)));
            String linea = null;
            int i = 0;
            while ((linea = buffer.readLine()) != null){
                String[] partes=linea.split(":");
                if(partes.length==2){
                    String dni = partes[0];
                    String nombre = partes[1];
                    Alumno alumno = new Alumno(dni, nombre);
                    alumnos.add(alumno);
                    modeloLista.add(i,dni + ": " + nombre);
                    ++i;
                }
            }
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (buffer != null) {
                try {
                    buffer.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
    }

    public void leerFicheroXML(String ruta) {
        alumnos.clear();
        alumnos.clear();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document documento = builder.parse(new File(ruta));
            NodeList alms = documento.getElementsByTagName("alumnos");
            for (int i = 0; i < alms.getLength(); i++) {
                Node alumno = alms.item(i);
                Element elemento = (Element) alumno;
                String dni = elemento.getElementsByTagName("dni").item(0)
                        .getChildNodes().item(0).getNodeValue();
                String nombre = elemento.getElementsByTagName("precio").item(0)
                        .getChildNodes().item(0).getNodeValue();
                Alumno aux = new Alumno(dni,nombre);
                alumnos.add(aux);
                modeloLista.add(i,dni + ": " + nombre);

            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    public void deserializarAlumnos(String ruta){
        alumnos.clear();
        modeloLista.clear();
        ObjectInputStream deserializador = null;
        try {
            deserializador = new ObjectInputStream(new FileInputStream(ruta));
            alumnos = (ArrayList<Alumno>) deserializador.readObject();
            for (int i = 0; i<alumnos.size();i++) {
                Alumno alumno = alumnos.get(i);
                String nombre = alumno.getNombre();
                String dni = alumno.getDNI();
                modeloLista.add(i,dni + ": " + nombre);
                System.out.println("Dni: " + alumno.getDNI() + " - Nombre: " + alumno.getNombre());
            }
        } catch (FileNotFoundException fnfe ) {
            fnfe.printStackTrace();
        } catch (ClassNotFoundException cnfe ) {
            cnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (deserializador != null)
                try {
                    deserializador.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
        }

    }

    public void escribirFicheroTextoPlano(String ruta) {
        FileWriter fichero = null;
        PrintWriter escritor = null;

        try {
            fichero = new FileWriter(ruta);
            escritor = new PrintWriter(fichero);
            for (Alumno a: alumnos) {
                escritor.println(a.getDNI()+":"+a.getNombre());
            }
            escritor.println("Primera línea de fichero para el primer ejemplo");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (fichero != null) {
                try {
                    fichero.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
    }

    public void escribirFicheroXML(String ruta) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            DOMImplementation dom = builder.getDOMImplementation();
            Document documento = dom.createDocument(null, "Alumnos", null);
            Element raiz = documento.createElement("alumnos");
            documento.getDocumentElement().appendChild(raiz);
            Element nodoAlumno = null, nodoDatos = null;
            Text texto = null;

            for (Alumno alumno : alumnos) {
                nodoAlumno = documento.createElement("alumno");
                raiz.appendChild(nodoAlumno);
                nodoDatos = documento.createElement("dni");
                nodoAlumno.appendChild(nodoDatos);
                texto = documento.createTextNode(alumno.getDNI());
                nodoDatos.appendChild(texto);
                nodoDatos = documento.createElement("nombre");
                nodoAlumno.appendChild(nodoDatos);
                texto = documento.createTextNode(alumno.getNombre());
                nodoDatos.appendChild(texto);
            }
            Source fuente = new DOMSource(documento);
            Result resultado = new StreamResult(new java.io.File(ruta));
            Transformer transformador = TransformerFactory.newInstance().newTransformer();
            transformador.transform(fuente, resultado);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public void serializarAlumnos(String ruta){
        ObjectOutputStream serializador = null;
        try {
            serializador = new ObjectOutputStream(new FileOutputStream(ruta));
            serializador.writeObject(alumnos);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (serializador != null)
                try {
                    serializador.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        switch(action){
            case "Nuevo":
                nuevoAlumno();
                break;
            case "Guardar":
                guardarAlumno();
                break;
            case "Eliminar":
                eliminarAlumno();
                break;
            case "Importar":
                importarAlumnos();
                break;
            case "Exportar":
                exportarAlumnos();
                break;
            default: break;
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if(!e.getValueIsAdjusting()){
            int aux=vista.getListaAlumnos().getSelectedIndex();
            if((aux >= 0)&&(aux < alumnos.size())){
                posicion=aux;
                Alumno alumno = alumnos.get(posicion);
                vista.getTfNombre().setText(alumno.getNombre());
                vista.getTfDNI().setText(alumno.getDNI());
            }
        }
    }
}
