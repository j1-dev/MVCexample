import javax.swing.*;

public class Principal {
    private JPanel panelPrincipal;
    private JTextField tfDNI;
    private JTextField tfNombre;
    private JList listaAlumnos;
    private JButton btImportar;
    private JButton btGuardar;
    private JButton btNuevo;
    private JButton btEliminar;
    private JButton btExportar;

    public Principal() {
        JFrame frame = new JFrame("Main");
        frame.setContentPane(panelPrincipal);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }

    public void setPanelPrincipal(JPanel panelPrincipal) {
        this.panelPrincipal = panelPrincipal;
    }

    public JTextField getTfDNI() {
        return tfDNI;
    }

    public void setTfDNI(JTextField tfDNI) {
        this.tfDNI = tfDNI;
    }

    public JTextField getTfNombre() {
        return tfNombre;
    }

    public void setTfNombre(JTextField tfNombre) {
        this.tfNombre = tfNombre;
    }

    public JList getListaAlumnos() {
        return listaAlumnos;
    }

    public void setListaAlumnos(JList listaAlumnos) {
        this.listaAlumnos = listaAlumnos;
    }

    public JButton getBtImportar() {
        return btImportar;
    }

    public void setBtImportar(JButton btImportar) {
        this.btImportar = btImportar;
    }

    public JButton getBtGuardar() {
        return btGuardar;
    }

    public void setBtGuardar(JButton btGuardar) {
        this.btGuardar = btGuardar;
    }

    public JButton getBtNuevo() {
        return btNuevo;
    }

    public void setBtNuevo(JButton btNuevo) {
        this.btNuevo = btNuevo;
    }

    public JButton getBtEliminar() {
        return btEliminar;
    }

    public void setBtEliminar(JButton btEliminar) {
        this.btEliminar = btEliminar;
    }

    public JButton getBtExportar() {
        return btExportar;
    }

    public void setBtExportar(JButton btExportar) {
        this.btExportar = btExportar;
    }
}
