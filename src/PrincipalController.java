import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    }

    private void exportarAlumnos(){

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
