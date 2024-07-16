package ij;

/* Juntos.java
 *
 * Copyright (c) Max Bylesjö, 2012-2020
 *
 * A class with functions to perform basic grayscale image analysis
 * procedures.
 *
 * This file is part of Macgrainij.
 *
 * Macgrainij is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 2, as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA.
 *
 * Software requirements:
 * *Java 1.4.x JRE or later
 *  http://java.sun.com/javase/downloads/
 * *Java Advanced ImagEJ 1.52 or later
 *  https://imagej.nih.gov/ij/download.html
 *
 */
import ij.gui.ImageWindow;
import ij.gui.Line;
import ij.gui.Overlay;
import ij.gui.Roi;
import ij.gui.Arrow;
import ij.gui.ShapeRoi;
import ij.gui.TextRoi;
import ij.gui.Toolbar;
import ij.gui.WaitForUserDialog;
import ij.plugin.*;
import ij.plugin.filter.BackgroundSubtracter;
import ij.plugin.filter.Color_Transformer;

import ij.process.AutoThresholder;
import ij.process.Blitter;
import ij.process.ByteProcessor;
import ij.process.ColorSpaceConverter;
import ij.process.FloatPolygon;
import ij.process.FloatProcessor;
import ij.process.FloodFiller;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;
import ij.process.ImageStatistics;
import ij.process.StackStatistics;

import ij.io.FileInfo;
import ij.io.FileSaver;
import ij.io.Opener;

import ij.measure.Calibration;
import ij.measure.Measurements;
import ij.plugin.frame.RoiManager;
import ij.text.TextWindow;

import java.awt.Frame;
import java.awt.Image;
import java.text.DecimalFormat;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import ij.plugin.filter.Analyzer;
import ij.measure.ResultsTable;
import ij.plugin.filter.EDM;
import ij.plugin.filter.MaximumFinder;
import ij.plugin.filter.ParticleAnalyzer;
import ij.plugin.filter.RankFilters;
import ij.plugin.filter.ThresholdToSelection;
import ij.process.ColorProcessor;
import ij.process.LUT;
import ij.process.StackConverter;
import java.awt.BorderLayout;
import java.awt.Color;

//import ij.Menus;
//import java.awt.*;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.SystemColor;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.image.IndexColorModel;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.time.Duration;
import java.util.StringTokenizer;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

//import javax.swing.*;
import javax.swing.border.*;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.table.*;
import javax.swing.table.JTableHeader;
import javax.swing.UIManager;
import static javax.swing.text.StyleConstants.Alignment;

import jxl.*;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.CellFormat;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableCell;
import jxl.write.WritableWorkbook;

import ch.rabanti.picoxlsx4j.Workbook;
import ch.rabanti.picoxlsx4j.style.BasicStyles;
import ch.rabanti.picoxlsx4j.style.CellXf;
import ch.rabanti.picoxlsx4j.style.Style;
import ch.rabanti.picoxlsx4j.Metadata;
import com.github.jferard.fastods.AnonymousOdsFileWriter;
import com.github.jferard.fastods.OdsDocument;
import com.github.jferard.fastods.OdsFactory;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.TableRowImpl;
import com.github.jferard.fastods.attribute.CellAlign;
import com.github.jferard.fastods.attribute.Length;
import com.github.jferard.fastods.attribute.SimpleColor;
import com.github.jferard.fastods.attribute.SimpleLength;
import com.github.jferard.fastods.attribute.VerticalAlign;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.style.TableRowStyle;
import ij.gui.ImageCanvas;
import ij.gui.PolygonRoi;
import ij.gui.Wand;
import ij.process.BinaryProcessor;
import ij.util.SortRoisByArea;
import ij.util.StringSorter;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Polygon;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Vector;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;

//import net.minidev.xlsx.XlsxBuilder;
public class Juntos implements ActionListener, MouseListener, WindowListener, PropertyChangeListener {

    public static final String MACGIJVERSION = "ver. 1.0";
    private static String[] extensiones = {"jpg", "bmp", "jpeg", "JPG", "png"};
    private static FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos de imagen admitidos", extensiones);
    private File lastdire, csvFile;
    private Frame Frlog;
    private ImagePlus imp, impcali, colorearim, imp2;
    public static ImageCanvas canvas;
    public static ImageWindow win;
    private BufferedImage image;
    public static BufferedImage bufferedImage;
    private RoiManager rm = null;
    private Overlay overlay;
    private TextRoi roilab;

    public static final int ApIm = 0, AfIm = 1, PeIm = 2, RuIm = 3;
    private int ejecuta, opanalisis;
    public String etiqueta;
    private static String etiquetas = " RGB";
    private DefaultTableModel modelo;
    //private static final DefaultTableModel resumen = new DefaultTableModel();
    public static final DefaultTableModel resumen = new DefaultTableModel();

    private String rowstring, value;
    private Clipboard system;
    private Clipboard clipboard;
    private StringSelection stsel, stselcn;
    public static JTable tableres;
    private JTable table = new JTable();
    //private final JTable tableres = new JTable(resumen);
    private JScrollPane screpane = new JScrollPane();
    private static JScrollPane scrollPane = new JScrollPane(); //agrega a lo guey como final
    //private JScrollPane scrollPane= new JScrollPane();
    private JFrame myJFrame = new JFrame();
    private static final JFrame Fresumen = new JFrame("Resumen Conteo");
    private DecimalFormat dec;
    /*
	private static JTable table = new JTable();
	private JTable tableres;
	private static final JScrollPane screpane = new JScrollPane(table);
	//private static final JScrollPane scrollPane= new JScrollPane(tableres);
	private static final JScrollPane scrollPane= new JScrollPane();
	private static final JFrame myJFrame = new JFrame();
	private static final JFrame Fresumen = new JFrame("Resumen Conteo");
     */
    private double minart, maxart, umbralseg;
    private int umbseg, resol;
    private boolean guarda_ima, vpi, verre, verrer;
    private double Ts, artg, volume, quebra, prom, dispersion;
    private double mart, art, lar, anc, per, queb, cir, asp, pro;
    private double rojos, verdes, azules;
    private double Tsu, artgr, vol, arge, arget;
    private int nge, graq, grae;
    private double ancpix, pixpuni, anchura, altura, tolerancia, modam;
    public static double iniciop, time, timefin, timef, ellapsedTime, finpro;
    public static int nimage;
    private short precision = 3;
    private Calibration calibration;
    private static Calibration globalCalibration;
    private static long finproc, startTime;
    private boolean noUpdateMode;
    private String path, extension, unidad, filename, unidadselect;
    private String dirname, name, lbl, ext, xlsFile, ers;
    public static String et1, et2, et3, et4;
    private Opener op;
    protected String units, unit, labelumbral;
    protected JOptionPane panes;
    JFileChooser chooser;
    //protected ImagePlus imp;
    protected int i, j, n, m, area, perimetro, circularidad, largo, ancho, alto, aspecto, res, col, totimage;
    String Prueba = "Prueba";
    String PROCESADAS = "Procesadas";
    public static String dir, dire, sdir;
    private int nc;
    private ResultsTable rt, rt3, ResultTable, ResumenConteo, mmResults, Resultados;
    static WaitForUserDialog waitForUserDialog;
    private final String okEtiqueta = "  Aceptar  ";
    //public static JProgressBar progressBar, Progressbar;
    //private static final JFrame f = new JFrame("solo sirve para poner icono");
    public static final Color fondopanel = Color.decode("#C89600");
    public static final JPopupMenu popupmenu = new JPopupMenu("Editar");
    public static JMenuItem item;
    public static JMenuItem cut, copyn, copyt, pasten, del, save, savec, rename, copyc;
    public ImageIcon icon1, icona, iconb, iconc, icond, icone, iconf, avanc, icong, indica, iconsob;
    public ImageIcon icon = new ImageIcon(getClass().getResource("/MACGRAIN-IJ.gif"));
    public ImageIcon icon2 = new ImageIcon(getClass().getResource("/macros/icons/error.gif"));
    //public Image indica; 
    ImageProcessor ip;
    private Color_Transformer lab;
    Color_Transformer tci = new Color_Transformer();
    public JLabel indicador = new JLabel();
    private List<String> m_items = new ArrayList<String>();
    //private static ImageJ imageJ = Interfazmac.imagej;
    final Preferences prefs = Preferences.userNodeForPackage(ij.Juntos.class);
    public static final int MAX_OPEN_RECENT_ITEMS = 15;
    String arg, tfaltante;
    public static Thread thread;
    private static LUT glasbeyLut;
    private IndexColorModel lut = null;
    private static int bgcol = 0;
    int size;
    boolean done;
    public MenuItem guardim, guardimc, guardimd;

    /*     
        public Juntos() { 
inicio(); 
reloj(); 
} 
    private void inicio() {
	this.imp = imp;
	etiqueta=" RGB";
	abrir();}    
    private void reloj() {
	this.imp = imp;
	etiqueta=" RGB";
	abrir();}
     */
    public void setEjecuta(int ejecuta, String spcolor) {

        Interfazmac.progressBar.setValue(0);
        Interfazmac.progressBar.setString("Inició el procesamiento");
        Interfazmac.progressBar.setStringPainted(true);
        Interfazmac.progressBar.setVisible(true);

        /*
         if (Interfazmac.tfmin.getText().isEmpty() || Interfazmac.tfmax.getText().isEmpty()){
        String tfaltante;
        if(Interfazmac.tfmin.getText().isEmpty()){ tfaltante="mínimo";} else { tfaltante="máximo";}
	String[] options = {"Aceptar"};
	JOptionPane panes = new JOptionPane("Se requiere regresar a modificar el tamaño "+tfaltante+" del grano.\n El valor se encuentra vacio.", 
	JOptionPane.ERROR_MESSAGE, 0,  icon2, options, options[0]);
	JDialog dialogs = panes.createDialog(null, "Definir el tamaño "+tfaltante+" del grano");  
	dialogs.setIconImage((icon).getImage());
	dialogs.setAlwaysOnTop(true);  
	dialogs.setVisible(true); 
        Toolkit.getDefaultToolkit().beep();
        return; }
         */
        //Interfazmac.tfmin.setName("mínimo");
        //Interfazmac.tfmax.setName("máximo");
        JTextField[] txtFields = new JTextField[2];
        txtFields[0] = Interfazmac.tfmin;
        txtFields[1] = Interfazmac.tfmax;

        // in action event
        for (JTextField txtField : txtFields) {
            if (txtField.getText().equals("") || txtField.getText().isEmpty() || txtField.getText().equals("0")
                    || txtField.getText() == null || Double.parseDouble(txtField.getText()) == 0) {
                tfaltante = txtField.getName();
                String sittat = (txtField.getText().isEmpty()) ? "está vacio." : "es igual a cero.";
                //txtField.setFont(new Font("Monospaced", Font.BOLD, 18));
                txtField.setText("");
                Toolkit.getDefaultToolkit().beep();
                //if((Double.parseDouble(Interfazmac.tfmax.getText())<=Double.parseDouble(Interfazmac.tfmin.getText()))) {tminsumax="\n El tamaño mínimo del grano no debe ser igual o menor a cero, ni igual o superior al máximo.";} else { tminsumax="";}   
                String[] options = {"Aceptar"};
                panes = new JOptionPane("<html>El tamaño <b>" + tfaltante + "</b> del grano <b>" + sittat + "</b></html>",
                        JOptionPane.ERROR_MESSAGE, 0, icon2, options, options[0]);
                JDialog dialogs = panes.createDialog(null, "Definir el tamaño " + tfaltante + " del grano");
                dialogs.setIconImage((icon).getImage());
                dialogs.setAlwaysOnTop(true);
                dialogs.setVisible(true);
                dialogs.dispose();
                txtField.requestFocusInWindow();
                return;
            }
        }

        if ((Double.parseDouble(Interfazmac.tfmax.getText()) <= Double.parseDouble(Interfazmac.tfmin.getText()))) {
            String comparamaxmin = (Double.parseDouble(Interfazmac.tfmax.getText()) == Double.parseDouble(Interfazmac.tfmin.getText())) ? "es igual que" : "es mayor que";
            Toolkit.getDefaultToolkit().beep();
            //if((Double.parseDouble(Interfazmac.tfmax.getText())<=Double.parseDouble(Interfazmac.tfmin.getText()))) {tminsumax="\n El tamaño mínimo del grano no debe ser igual o menor a cero, ni igual o superior al máximo.";} else { tminsumax="";}   
            String[] options = {"Aceptar"};
            panes = new JOptionPane("<html>El tamaño mínimo del grano no debe ser igual o superior al máximo.</p>"
                    + "<p>El mínimo (" + Double.parseDouble(Interfazmac.tfmin.getText()) + ") <b>" + comparamaxmin + "</b> el máximo (" + Double.parseDouble(Interfazmac.tfmax.getText()) + "), modifique según sea necesario.</html>",
                    JOptionPane.ERROR_MESSAGE, 0, icon2, options, options[0]);
            JDialog dialogs = panes.createDialog(null, "Definir tamaño del grano");
            dialogs.setIconImage((icon).getImage());
            dialogs.setAlwaysOnTop(true);
            dialogs.setVisible(true);
            dialogs.dispose();
            Interfazmac.tfmax.requestFocusInWindow();
            Interfazmac.tfmax.setCaretPosition(Interfazmac.tfmax.getText().length());
            return;
        }

        etiqueta = spcolor;

        switch (ejecuta) {
            case ApIm:
                this.imp = imp;
                Interfazmac.statusBar.setText("Seleccionó procesar una imagen en RGB");
                IJ.showStatus("Seleccionó procesar una imagen en RGB");
                abrirIm();
                break;

            case AfIm:
                this.imp = imp;
                Interfazmac.statusBar.setText("Seleccionó procesar todas las imágenes en RGB");
                IJ.showStatus("Seleccionó procesar todas las imágenes en RGB");
                abrirFolder();
                break;

            case PeIm:

                System.out.println("Procesar esta imagen en" + lbl);
                imp = IJ.getImage();
                FileInfo fi = imp.getOriginalFileInfo();
                dir = fi.directory;
                ext = imp.getTitle().substring(imp.getTitle().lastIndexOf(".") + 1, imp.getTitle().length());
                lbl = imp.getTitle().substring(imp.getTitle().lastIndexOf("/") + 1, imp.getTitle().lastIndexOf("."));
                imp.getWindow().setVisible(false);

                medirGranosTrigo();
                break;

            case RuIm:
                //Opener op = new Opener();
                //op.setSilentMode(true);
                //System.out.println(Interfazmac.abrirrec.getItem(0).getText().toString());
                //System.out.println(Interfazmac.settings.iniProperties.getProperty("LastOut.Dir", ""));

                if (Interfazmac.abrirrec.getItemCount() >= 1) {

                    imp = new Opener().openImage(Interfazmac.abrirrec.getItem(0).getText().toString());
                    //System.out.println(Interfazmac.settings.iniProperties.getProperty("Espacio.Color", etiqueta));
                    if (imp != null) {

                        fi = imp.getOriginalFileInfo();
                        dir = fi.directory;
                        ext = imp.getTitle().substring(imp.getTitle().lastIndexOf(".") + 1, imp.getTitle().length());
                        lbl = imp.getTitle().substring(imp.getTitle().lastIndexOf("/") + 1, imp.getTitle().lastIndexOf("."));

                        nimage = 1;
                        timef = 0;
                        medirGranosTrigo();
                    } else {
                        String title;
                        title = "Error: No existe la imagen.";
                        String msg = "La última imagen utilizada ya no se encuentra disponible, utilizar otra opción de análisis.";
                        showMessageBox(title, msg, 300, "Aceptar", null);
                        Toolkit.getDefaultToolkit().beep();
                        Interfazmac.statusBar.setText("No hay imagen abierta");
                        return;
                    }

                } else {
                    String title;
                    title = "Error: No existe imagen previa.";
                    String msg = "No existe registro de una última imagen utilizada, utilizar otra opción de análisis.";
                    showMessageBox(title, msg, 300, "Aceptar", null);
                    Toolkit.getDefaultToolkit().beep();
                    Interfazmac.statusBar.setText("No hay imagen abierta");
                    return;
                }

                break;

        }
        Frlog = WindowManager.getFrame("Log");
        if (Frlog instanceof TextWindow) {
            ((TextWindow) Frlog).setTitle("Registro de eventos");
            //((TextWindow) Frlog).setIconImage((icon).getImage());
            Frlog.setMenuBar(Interfazmac.mblog);
            Frlog.setIconImage((icon).getImage());
            Frlog.setLocationRelativeTo(Interfazmac.myJFrame);
            Frlog.setAlwaysOnTop(true);
            //Frlog.setVisible(false); //utilizarlo para activar o desactivar todos los ij.log
        }

    }

    public void abrirIm() {

        indicador.setVisible(false);
//
        closeAll();
        nimage = 0;
        //imageJ.getProgressBar().setVisible(true);
        IJ.showProgress(0, 20);

        //Preferences pref = Preferences.userRoot();
        //String lastdire = pref.get("DEFAULT_PATH", "");
        //String lastdire = Prefs.get("LastInDir", "");
        String lastdire = Interfazmac.settings.iniProperties.getProperty("LastInDir", "");
        //dir = lastdire+Prefs.getFileSeparator();
        this.imp = imp;
        JFileChooser chooser = new JFileChooser();
        chooser.setLocale(Locale.getDefault());
        chooser.setPreferredSize(new Dimension(800, 600));
        chooser.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, fondopanel));
        chooser.setApproveButtonText("Procesar");
        chooser.setApproveButtonMnemonic('p');
        chooser.setApproveButtonToolTipText("Procesar imagen seleccionada");
        chooser.addChoosableFileFilter(filter);
        chooser.setFileFilter(filter);
        chooser.setDialogTitle("Abrir imagen a procesar en " + etiqueta);
        chooser.setCurrentDirectory(new File(lastdire));
        int actionDialog = chooser.showOpenDialog(Interfazmac.myJFrame);
        chooser.setAcceptAllFileFilterUsed(true);
        dir = chooser.getCurrentDirectory().getPath() + File.separator;
        //dir = chooser.getCurrentDirectory().getPath()+Prefs.getFileSeparator();
        if (actionDialog == JFileChooser.APPROVE_OPTION) {

            File file = chooser.getSelectedFile();
            //pref.put("LastOutDir", file.getAbsolutePath());
            //Prefs.set("LastInDir", file.getAbsolutePath());
            Interfazmac.settings.iniProperties.put("Espacio.Color", etiqueta);
            Interfazmac.settings.iniProperties.put("LastInDir", file.getAbsolutePath());
            Interfazmac.settings.writeIni();
            //Prefs.set("LastOutDir", file.getAbsolutePath() );
            //Prefs.savePreferences();
            String recientes = file.getAbsolutePath();

            //obtener nombre y estensión
            //String name = file.getName();
            ext = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length());
            lbl = file.getName().substring(file.getName().lastIndexOf("/") + 1, file.getName().lastIndexOf("."));
            path = dir + file.getName();

            //String exten = chooser.getTypeDescription(file);
            //chooser.setCurrentDirectory(file);
            IJ.showStatus("El análisis de la imagen " + lbl + " se inició en " + etiqueta);
            Interfazmac.statusBar.setText("El análisis de la imagen " + lbl + " se inició en " + etiqueta);
            //IJ.log("La imagen "+lbl+" se seleccinó para su análisis" +etiqueta);
            //IJ.log(lbl+"  **  "+ext);
            Opener opener = new Opener();
            //opener.openAndAddToRecent(path);
            //opener.addOpenRecentItem(path);
            Menus.addOpenRecentItem(path);
            opener.setSilentMode(true);

            nimage = nimage + 1;
            imp = opener.openImage(dir, file.getName());
            //String recientes =setLastOpenFilePath(chooser.getCurrentDirectory().getPath());
            if (imp != null) {

                
                /*
                        ImageJ imageJ = Interfazmac.imagej;
                        if (imageJ == null) {
                            return;
                        }
                 */

        Menus.addOpenRecentItem(path);
        Interfazmac.abrirrec.add(path);

        int count = Interfazmac.abrirrec.getItemCount();
        for (int i = 0; i < count;) {
            if (Interfazmac.abrirrec.getItem(i).getText().equals(path)) {
                Interfazmac.abrirrec.remove(i);
                count--;

            } else {
                i++;
            }
        }
        if (count == MAX_OPEN_RECENT_ITEMS) {
            Interfazmac.abrirrec.remove(MAX_OPEN_RECENT_ITEMS - 1);
        }
        item = new JMenuItem(path);
        Interfazmac.abrirrec.insert(item, 0);
        item.addActionListener(this);
        item.setActionCommand("ABRIRICA");
        int n = Interfazmac.abrirrec.getItemCount();
        for (int ix = 0; ix < n; ix++) {
            String key = "" + ix;
            if (key.length() == 1) {
                key = "0" + key;
            }
            key = "reciente" + key;
            Interfazmac.settings.iniProperties.put(key, Interfazmac.abrirrec.getItem(ix).getText());
            Interfazmac.settings.writeIni();
        }
                //Prefs.savePreferences();
                this.imp = imp;
                medirGranosTrigo();

            } else {
                JOptionPane.showMessageDialog(Interfazmac.myJFrame,
                        "El archivo seleccionado no es una imagen.\n El archivo puede estar dañado.",
                        "Error de lectura de imagen",
                        JOptionPane.ERROR_MESSAGE, icon2);
                return;
            }
        } else {
            Toolkit.getDefaultToolkit().beep();
            IJ.showStatus("Canceló el análisis de la imagen en " + etiqueta + " (???) (=_=)");
            Interfazmac.statusBar.setText("Canceló el análisis de la imagen en " + etiqueta + " (???) (=_=) ¯\\_(?)_/¯");
            //IJ.log("El analisis en "+etiqueta+" de la imagen se canceló");
        }

    }

    void abrirFolder() {

        closeAll();
        totimage = 0;
        Prefs.useJFileChooser = true;
        IJ.showProgress(0, 20);
        //Preferences pref = Preferences.userRoot();
        //String lastdire = pref.get("DEFAULT_PATH", "");
        String lastdire = Interfazmac.settings.iniProperties.getProperty("LastInDir", "");
        //dir = pref.get("DEFAULT_PATH", "");
        this.imp = imp;
        JFileChooser chooser = new JFileChooser();
        chooser.setLocale(Locale.getDefault());
        chooser.setPreferredSize(new Dimension(800, 600));
        chooser.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, fondopanel));
        chooser.setApproveButtonText("Procesar Carpeta");
        chooser.setApproveButtonMnemonic('p');
        chooser.setApproveButtonToolTipText("Procesar las imágenes dentro de la carpeta seleccionada");
        chooser.addChoosableFileFilter(filter);
        chooser.setFileFilter(filter);
        chooser.setMultiSelectionEnabled(true);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //chooser.setDialogType(JFileChooser.OPEN_DIALOG);
        //dir = lastdire+Prefs.getFileSeparator();
        chooser.setAcceptAllFileFilterUsed(false);

        chooser.setFileFilter(filter);
        chooser.setDialogTitle("Seleccionar directorio con imagenes a procesar en " + etiqueta);
        chooser.setCurrentDirectory(new File(lastdire));
        dir = chooser.getCurrentDirectory().getPath() + File.separator;
        int actionDialog = chooser.showOpenDialog(Interfazmac.myJFrame);

        if (actionDialog == JFileChooser.APPROVE_OPTION) {
            IJ.showStatus("El análisis de las imágenes se inició en " + etiqueta);
            Interfazmac.statusBar.setText("El análisis de las imágenes se inició en " + etiqueta);
            File files = chooser.getSelectedFile();
            //pref.put("DEFAULT_PATH", files.getAbsolutePath());
            Interfazmac.settings.iniProperties.put("Espacio.Color", etiqueta);
            Interfazmac.settings.iniProperties.put("LastInDir", files.getAbsolutePath());
            Interfazmac.settings.writeIni();
            File[] listas = files.listFiles();
            String[] list = new File(dir).list();
            //int n = list.length;

            totimage = 0;
            for (File fileimg : listas) {
                if (fileimg.isFile() && fileimg.getName().endsWith(".jpg") || fileimg.getName().endsWith(".jpeg") || fileimg.getName().endsWith(".bmp") || fileimg.getName().endsWith(".png")) {
                    //totimage = totimage + 1;
                    totimage++;
                }
            }

            if (totimage == 0) {
                String title;
                if (listas.length == 0) {
                    title = "Error: El directorio está vacio.";
                } else {
                    title = "Error: El directorio no contiene imágenes.";
                }
                String msg = "Seleccionar otro directorio que contenga imágenes con extensión admitida (jpg, jpeg, bmp, png).";
                showMessageBox(title, msg, 300, "Aceptar", null);
                Toolkit.getDefaultToolkit().beep();
                return;
            }

            if (listas.length == 0) { // getSelectedFiles does not work on some JVMs
                listas = new File[1];
                listas[0] = chooser.getSelectedFile();
                Toolkit.getDefaultToolkit().beep();
            }

            nimage = 0;
            timef = 0;
            sdir = chooser.getCurrentDirectory().toString();
            //String dir = chooser.getCurrentDirectory().getPath()+Prefs.getFileSeparator();
            Opener opener = new Opener();
            opener.setSilentMode(true);

            for (int i = 0; i < listas.length; i++) {
                //int nimages=listas.length;
                File currentFile = listas[i];

                //if (currentFile.isFile() && currentFile.getName().endsWith(".jpg")) {
                //if (currentFile.isFile() && currentFile.getName().matches("(?i).*\\.(jpg|jpeg|bmp|png)$")){
                if (currentFile.isFile() && currentFile.getName().endsWith(".jpg") || currentFile.getName().endsWith(".jpeg") || currentFile.getName().endsWith(".bmp") || currentFile.getName().endsWith(".png")) {

                    //if (files[i].isFile()||files[i].getName().endsWith( ".jpg" ) || name.endsWith( ".jpeg" || name.endsWith( ".bmp" || name.endsWith( ".png")
                    nimage++;
                    //nimage = listas.length;
                    System.out.println(nimage + " " + totimage);
                    //String name = currentFile.getName();
                    //obtener nombre y estensión
                    ext = currentFile.getName().substring(currentFile.getName().lastIndexOf(".") + 1, currentFile.getName().length());
                    lbl = currentFile.getName().substring(currentFile.getName().lastIndexOf("/") + 1, currentFile.getName().lastIndexOf("."));
System.out.println("Archivo procesado "+lbl);
                    //String exten = name.substring(name.lastIndexOf("."),name.length());
                    //lbl = name.replace(exten, "");
                    //ext = name.replace(lbl+".", "");
                    //IJ.log(lbl+" "+ext);
                    //path = dir + currentFile.getName();
                    path = currentFile.getAbsolutePath();
                    //IJ.log("Se analizaron en total: " +nimages);

                    //Opener opener = new Opener();
                    //imp = opener.openImage(lolo);
                    //ImagePlus imp = opener.openImage(dir, files[i].getName());
                    //imp = !dir.endsWith("/") ? new Opener().openImage(path) : null;
                    if (currentFile.isFile()) {
                        imp = (new Opener()).openImage(path);
                        //ip = imp.getProcessor();
                        //imp.show();

                        /*
                        ImageJ imageJ = Interfazmac.imagej;
                        if (imageJ == null) {
                            return;
                        }
                         */
                                Menus.addOpenRecentItem(path);
        Interfazmac.abrirrec.add(path);

        int count = Interfazmac.abrirrec.getItemCount();
        for (int c = 0; c < count;) {
            if (Interfazmac.abrirrec.getItem(c).getText().equals(path)) {
                Interfazmac.abrirrec.remove(c);
                count--;

            } else {
                c++;
            }
        }
        if (count == MAX_OPEN_RECENT_ITEMS) {
            Interfazmac.abrirrec.remove(MAX_OPEN_RECENT_ITEMS - 1);
        }
        item = new JMenuItem(path);
        Interfazmac.abrirrec.insert(item, 0);
        item.addActionListener(this);
        item.setActionCommand("ABRIRICA");
        int n = Interfazmac.abrirrec.getItemCount();
        for (int ix = 0; ix < n; ix++) {
            String key = "" + ix;
            if (key.length() == 1) {
                key = "0" + key;
            }
            key = "reciente" + key;
            Interfazmac.settings.iniProperties.put(key, Interfazmac.abrirrec.getItem(ix).getText());
            Interfazmac.settings.writeIni();
        }

                        //Prefs.savePreferences();
                        this.imp = imp;
                        medirGranosTrigo();
                    }
                }
            }

        } else {
            Toolkit.getDefaultToolkit().beep();
            IJ.showStatus("Canceló el análisis de la imágenes en" + etiqueta + " (???) (=_=)");
            Interfazmac.statusBar.setText("Canceló el análisis de la imágenes en" + etiqueta + " (???) (=_=) ¯\\_(?)_/¯");
        }

    }

    private void actualizarRecientes() {
        Menus.addOpenRecentItem(path);
        Interfazmac.abrirrec.add(path);

        int count = Interfazmac.abrirrec.getItemCount();
        for (int i = 0; i < count;) {
            if (Interfazmac.abrirrec.getItem(i).getText().equals(path)) {
                Interfazmac.abrirrec.remove(i);
                count--;

            } else {
                i++;
            }
        }
        if (count == MAX_OPEN_RECENT_ITEMS) {
            Interfazmac.abrirrec.remove(MAX_OPEN_RECENT_ITEMS - 1);
        }
        item = new JMenuItem(path);
        Interfazmac.abrirrec.insert(item, 0);
        item.addActionListener(this);
        item.setActionCommand("ABRIRICA");
        int n = Interfazmac.abrirrec.getItemCount();
        for (int ix = 0; ix < n; ix++) {
            String key = "" + ix;
            if (key.length() == 1) {
                key = "0" + key;
            }
            key = "reciente" + key;
            Interfazmac.settings.iniProperties.put(key, Interfazmac.abrirrec.getItem(ix).getText());
            Interfazmac.settings.writeIni();
        }
    }

    /*Se inicia el análisis de la imgen*/
    void medirGranosTrigo() {

        Thread processingThread = new Thread(new Runnable() {
            @Override
            public void run() {

                Interfazmac.panelima.clearImage();
                //Interfazmac.panelima.validate();
                Interfazmac.progressBar.setStringPainted(true);
                Interfazmac.progressBar.setVisible(true);
                Interfazmac.progressBar.setValue(0);
                Interfazmac.progressBar.setString("Procesando imagen");
                Interfazmac.progressBar.repaint();

                double inicioProceso = System.currentTimeMillis(); // Guardar el tiempo de inicio del proceso

// Tu código de procesamiento de la imagen aquí...
// Calcula el progreso del proceso y actualiza la barra de progreso
                double tiempoTranscurrido = System.currentTimeMillis() - inicioProceso;
                double progreso = Math.max(0, Math.min(tiempoTranscurrido / 100000, 1)); // Donde TIEMPO_TOTAL es la duración total del proceso
                int valorProgreso = (int) (progreso * 100);
                Interfazmac.progressBar.setValue(valorProgreso);

// Verificar si el proceso ha terminado
                if (progreso >= 1.0) {
                    Interfazmac.progressBar.setString("El procesamiento finalizó");
                }
                //System.out.println(processed);

                //Interfazmac.save.setEnabled(true);
                //Interfazmac.guardarima.setEnabled(true);
                //indicador= new JLabel(avanc);
                indicador.setVisible(true);
                //Interfazmac.pstatusbar.add(indicador);
                IJ.showProgress(1, 20);
                Interfazmac.progressBar.setValue(5);

                //System.out.println(etiqueta); // no necesario
                startTime = System.currentTimeMillis();
                iniciop = System.currentTimeMillis();

                //ImagePlus imp = IJ.openImage("");
                imp = imp;

                Calibration cali = imp.getCalibration();

                unidadselect = (String) Interfazmac.unidades.getSelectedItem();
                //String unitsi = cali.getUnit();		
                //String unidad = unidad;
                String unidadc = unidadselect + "²";
                //String unidadc = unitsi+"²";
                String unidadv = unidadselect + "³";
                //String unidadv = unitsi+"³";

                ////////////*Establecer escala*////////////
                anchura = imp.getWidth();
                altura = imp.getHeight();

                if (unidadselect.equals("mm")) {
                    cali.pixelWidth = 215.9 / anchura;
                    //cali.calibrated();
                } else {
                    cali.pixelWidth = 21.59 / anchura;
                    //System.out.println(anchura/215.9);
                }

                cali.pixelHeight = cali.pixelWidth * 1;
                cali.pixelDepth = cali.pixelWidth;
                cali.setUnit(unidadselect);
                imp.setCalibration(cali);
                imp.setGlobalCalibration(cali);

                //IJ.beep();
                //Calibration cali = imp.getCalibration();
                ancpix = cali.pixelWidth;
                /**
                 * Convierte una coordenada x en unidades físicas a píxeles.
                 */
                pixpuni = cali.getRawX(1.0);
                System.out.println(pixpuni);

                //IJ.run(imp, "Set Scale...", "distance="+anchura+" known=215.9 unit="+unidad+" global");
                //Calibration cal = imp!=null?imp.getCalibration():(new Calibration());
                //Calibration cal = imp.getCalibration();
                //String unit = cal.getUnit();
                //String unidad = unit;
                //String unidadc = unit+"²";
                //String unidadv = unit+"³";
                //if (unidad=="cm") {IJ.showStatus("Configuración establecida en cm");} else {IJ.showStatus("Configuración establecida en mm");}
                //IJ.beep();
                /////////////*Fin establecer escala*/////////////
                IJ.showProgress(2, 20);

                Interfazmac.progressBar.setValue(10);

                ////////////*Binarizar y segmentar imagen*////////////    
                IJ.showProgress(3, 20);

                //RESIZE
                //IJ.run(imp, "Canvas Size...", "width=" + anchura + 2 + " height=" + altura + 2 + " position=Center");
                //CanvasResizer cr= new CanvasResizer();
                //ImageProcessor newtam = cr.expandImage(imp.getProcessor(), (int)anchura + 2, (int)altura + 2, (int)((anchura + 2) - anchura)/2, (int)((altura + 2) - altura)/2);
                ImageProcessor newtam = imp.getProcessor().createProcessor((int) anchura + 2, (int) altura + 2);
                newtam.setColor(Color.BLACK);
                newtam.fill();
                newtam.insert(imp.getProcessor(), (int) ((anchura + 2) - anchura) / 2, (int) ((altura + 2) - altura) / 2);
                imp.setProcessor(null, newtam);

                ImagePlus copia = imp.duplicate();

                /* 
            if (anchura >= 3400) {
                //IJ.run(copia, "Smooth", ""); 
                //copia.getProcessor().smooth();
                
                //IJ.run(copia, "Multiply...", "value=1.25");
                copia.getProcessor().multiply(1.5);
                
                //copia.getProcessor().smooth();
                //IJ.run(copia, "Subtract Background...", "rolling=70");
                BackgroundSubtracter sutb = new BackgroundSubtracter();
                sutb.rollingBallBrightnessBackground((ColorProcessor) copia.getProcessor(), 70, false, false, false, true, true);
                
                //copia = CompositeConverter.makeComposite(copia);
                Prefs.blackBackground = true;
                //ImageStatistics stats = copia.getProcessor().getStats();
		//AutoThresholder thresholder = new AutoThresholder();
		//umbralseg = thresholder.getThreshold(Default, stats.histogram); 
                
                //CONVERTIR A 8 BITS
                //IJ.run(copia, "8-bit", "");
                //copia.getProcessor().convertToByte(true);   
                new ImageConverter(copia).convertToGray8();


                //SEGMENTACIÓN DE LA IMAGEN
                //IJ.run(copia, "Auto Threshold...", "method=Default white");
                //IJ.setAutoThreshold(copia, "Default white");
                //copia.getProcessor().setAutoThreshold("Default white");
                //copia.getProcessor().setAutoThreshold(ImageProcessor.ISODATA2, ImageProcessor.RED_LUT);
                //copia.getProcessor().setAutoThreshold(Default, Prefs.blackBackground, ImageProcessor.NO_LUT_UPDATE);
                copia.getProcessor().setThreshold(40, 255, ImageProcessor.NO_LUT_UPDATE);
                //CREAR MASCARA
                //copia.createMask();
                //IJ.run(copia, "Convert to Mask", "method=Default background=Dark");
                ByteProcessor mask = copia.getProcessor().createMask();
                //ByteProcessor mask = copia.createThresholdMask();
                copia.setProcessor(null, mask);
                
                //ELIMINAR RUIDO
                //IJ.run(copia, "Remove Outliers...", "radius=4 threshold=100 which=Bright");
                RankFilters rankFilters = new RankFilters();
                rankFilters.rank(copia.getProcessor(), 4,  RankFilters.OUTLIERS, 100, 0);        
                
                //IJ.run(copia, "Close-", "");
                int fg = Prefs.blackBackground ? 255 : 0;
                int foreground = copia.getProcessor().isInvertedLut() ? 255-fg : fg;
                int background = 255 - foreground;
                
                ((ByteProcessor)copia.getProcessor()).dilate(1, background);
                ((ByteProcessor)copia.getProcessor()).erode(1, background);
                

            }
                 */
                //if (anchura <= 2555) {
                //IJ.run(copia, "Multiply...", "value=1.25");
                copia.getProcessor().multiply(1.5);

                //copia.getProcessor().smooth();
                //IJ.run(copia, "Subtract Background...", "rolling=70");
                //BackgroundSubtracter sutb = new BackgroundSubtracter();
                new BackgroundSubtracter().rollingBallBrightnessBackground((ColorProcessor) copia.getProcessor(), 70, false, false, false, true, false);

                //if (Interfazmac.switch1.isOnOff()){
                Integer opumbs = (Interfazmac.unium.getSelectedItem().equals("Predeterminado")) ? 0 : (Interfazmac.unium.getSelectedItem().equals("Automático")) ? 1 : (Interfazmac.unium.getSelectedItem().equals("Arduo")) ? 2 : 3;
                Integer minthreshold = 0;
                switch (opumbs) {
                    case 0:
                        umbralseg = (int) Math.round(copia.getStatistics().stdDev);
                        //umbralseg = copia.getProcessor().getAutoThreshold(copia.getStatistics().histogram);
                        //System.out.println("Mean default : "+ umbralseg);
                        //copia.getProcessor().setAutoThreshold(AutoThresholder.Method.Default, !Prefs.blackBackground, ImageProcessor.NO_LUT_UPDATE);
                        labelumbral = "P";
                        break;
                    case 1:
                        umbralseg = (int) copia.getStatistics().mean;
                        //umbralseg = (int) Math.floor(stats.mean);
                        //copia.getProcessor().setThreshold(0, umbralseg, ImageProcessor.NO_LUT_UPDATE);
                        //copia.getProcessor().setThreshold(0, stats.mean, ImageProcessor.NO_LUT_UPDATE);
                        labelumbral = "A";
                        break;
                    case 2:

                        ImageStack stack2 = new ImageStack(imp.width, imp.height);
                        ColorProcessor cp = (ColorProcessor) imp.getStack().getProcessor(1);
                        stack2.addSlice("saturación", cp.getHSBStack().getProcessor(3));
                        copia.setStack("copia", stack2);

                        //copia.show();
                        //return;
                        //umbralseg = 70;
                        umbralseg = (int) Math.round(copia.getProcessor().getAutoThreshold());
                        //copia.getProcessor().setThreshold(0, umbralseg, ImageProcessor.NO_LUT_UPDATE);
                        labelumbral = "Arduo";
                        break;
                    case 3:
                        umbralseg = Integer.parseInt(Interfazmac.unium.getSelectedItem().toString());
                        //copia.getProcessor().setThreshold(0, umbralseg, ImageProcessor.NO_LUT_UPDATE);
                        labelumbral = "manual";
                        break;
                }
                //System.out.println("Mean default : "+ umbralseg+ " número de slices "+ (int)copia.nSlices);

                //se remplazó IJ.run(copia, "8-bit", "");
                new ImageConverter(copia).convertToGray8();
                Prefs.blackBackground = true;

                //IJ.setAutoThreshold(copia, "Default");
                //IJ.setRawThreshold(copia, 40, 255, null);
                //IJ.setThreshold(copia, umbralseg, 255);
                Roi rut = new Roi(0, 0, 20, 20);
                imp.setRoi(rut);
                //System.out.println("qué área tiene rut "+ rut.getStatistics().area);
                System.out.println("qué color hay " + rut.getStatistics().mean);
                if (rut.getStatistics().mean >= 100) {
                    ImageStack stack2 = new ImageStack(imp.width, imp.height);
                    ColorProcessor cp = (ColorProcessor) imp.getStack().getProcessor(1);
                    stack2.addSlice("saturación", cp.getHSBStack().getProcessor(3));
                    copia.setStack("copia", stack2);
                    copia.getProcessor().setThreshold(0, (int) Math.round(copia.getProcessor().getAutoThreshold()), ImageProcessor.NO_LUT_UPDATE);
                    //copia.getProcessor().invert();
                    Prefs.blackBackground = true;
                    //imp.resetRoi();
                    imp.changes = false;

                } else {
                    copia.getProcessor().setThreshold(umbralseg, 255, ImageProcessor.NO_LUT_UPDATE);
                }

                //copia.getProcessor().setThreshold(minthreshold, umbralseg, ImageProcessor.NO_LUT_UPDATE);
                //copia.getProcessor().setAutoThreshold(AutoThresholder.Method.Default, Prefs.blackBackground, ImageProcessor.NO_LUT_UPDATE);
                //copia.show();
                //IJ.setRawThreshold(copia, umbralseg, 255, null);
                //Crear máscara
                //IJ.run(copia, "Convert to Mask", "method=Default background=Dark only");
                //ByteProcessor mask = copia.getProcessor().createMask();
                ByteProcessor mask = copia.createThresholdMask();
                //Thresholder.setMethod("Default");
                //Thresholder.setBackground("Dark only");
                /*
                for (i=0; i<copia.getProcessor().getPixelCount(); i++) {
                if (mask.get(i)>0)
                copia.getProcessor().set(i,65535); //The Decimal color 65535 is a light color, and the websafe version is hex 00FFFF
                }
                 */
                //acelera el proceso en comparación al método anterior
                copia.setProcessor("copia", mask);
                //copia.getMask();
                //copia.updateAndDraw();
                //new ImagePlus(copia.getTitle(),copia.getProcessor());
                //copia.getProcessor().getPixels();;

                //IJ.run(copia, "Remove Outliers...", "radius=4 threshold=100 which=Bright slice");
                (new RankFilters()).rank(copia.getProcessor(), 4, RankFilters.OUTLIERS, 100, 0);
                //rankFilters.rank(copia.getProcessor(), 4, RankFilters.OUTLIERS, RankFilters.BRIGHT_OUTLIERS, 100);

                //IJ.run(copia, "Close-", "slice");
                int fg = Prefs.blackBackground ? 255 : 0;
                int foreground = copia.getProcessor().isInvertedLut() ? 255 - fg : fg;
                int background = 255 - foreground;

                ((ByteProcessor) copia.getProcessor()).dilate(3, background); //puede encontrar el pliege
                ((ByteProcessor) copia.getProcessor()).erode(3, background);

                //copia.updateImage();
                //FloodFiller floodFiller = new FloodFiller(copia.getProcessor());
                //copia.getProcessor().fill();
                //}
                //IJ.run("Colors...", "foreground=white background=white selection=blue");
                IJ.run("Input/Output...", "jpeg=50 gif=-1 file=.csv copy_column copy_row save_column save_row");
                FileSaver.setJpegQuality(50);
                Prefs.defaultResultsExtension();
                Prefs.copyColumnHeaders = true;
                Prefs.dontSaveHeaders = false;
                Prefs.dontSaveRowNumbers = false;
                //IJ.run("Labels...", "color=blue font=12 show bold");
                Interfazmac.progressBar.setValue(15);

                if (anchura >= 3400) {
                    tolerancia = 1.2;
                }
                if (anchura <= 1700) {
                    tolerancia = 0.4;
                } else {
                    tolerancia = 1;
                }
                /* 
           int widthi = copia.getProcessor().getWidth();
int heighti = copia.getProcessor().getHeight();
FloodFiller ff = new FloodFiller(copia.getProcessor());
copia.getProcessor().setColor(Color.WHITE); // Establecer el color de relleno como blanco
for (int y = 1; y < heighti - 1; y++) {
    for (int x = 1; x < widthi - 1; x++) {
        if (copia.getProcessor().getPixel(x, y) == Color.BLACK.getRGB()) {
            // Comprobar si al menos uno de los vecinos es blanco
            if (copia.getProcessor().getPixel(x - 1, y) == Color.WHITE.getRGB() ||
                copia.getProcessor().getPixel(x + 1, y) == Color.WHITE.getRGB() ||
                copia.getProcessor().getPixel(x, y - 1) == Color.WHITE.getRGB() ||
                copia.getProcessor().getPixel(x, y + 1) == Color.WHITE.getRGB()) {
                ff.fill(x, y);
            }
        }
    }
}

            //SEPARATION ROI WATHERSHED
            //Adjustable_Watershed separacion = new Adjustable_Watershed();
            //separacion.setTolerance(tolerancia); 
            //separacion.run(copia.getProcessor());
            /**
             * Se reemplazo el wathershed.
             *
             * @see ij.plugin.filter.MaximumFinder
             * @see ij.plugin.filter.EDM
             *
                 */

                //EDM wts = new EDM();
                //wts.toWatershed(copia.getProcessor());    
                //MaximumFinder maxFinder = new MaximumFinder();
                //FloatProcessor floatEdm = wts.makeFloatEDM(copia.getProcessor(), background, false);
                ((ByteProcessor) copia.getProcessor()).dilate(4, background);
                FloatProcessor floatEdm = (new EDM()).makeFloatEDM(copia.getProcessor(), background, false);
                ByteProcessor maxIp = (new MaximumFinder()).findMaxima(floatEdm, tolerancia, ImageProcessor.NO_THRESHOLD, MaximumFinder.SEGMENTED, false, true);
                if (maxIp != null) {
                    copia.getProcessor().copyBits(maxIp, 0, 0, Blitter.AND);
                    //copia.getProcessor().copyBits(maxIp, 0, 0, Blitter.SUBTRACT);
                }

                //((ByteProcessor) copia.getProcessor()).erode(2, background);
//SeparaGranos.realizaSeparaGranos(copia.getProcessor(), tolerancia, copia.getProcessor());
                //IJ.runUserPlugIn("Adjustable Watershed", "ij.plugin.filter.Adjustable_Watershed", "1", true);
                //IJ.runPlugIn(copia, "ij.plugin.filter.Adjustable_Watershed", "run tolerancia="+tolerancia);
                //IJ.runPlugIn(copia, "ij.plugin.filter.Adjustable_Watershed", "tolerancia="+tolerancia+"");
                //copia = (ImagePlus)IJ.runPlugIn("ij.plugin.filter.Adjustable_Watershed", "tolerancia="+tolerancia+"");
                ////////////*Fin de la segmentación*//////////// 
                //((ByteProcessor) copia.getProcessor()).erode(2, background); //verificar tiempo
                IJ.showProgress(4, 20);
                Interfazmac.progressBar.setValue(20);

                ////////////*Establecer medidas y analizar imagen*//////////// 
                Analyzer.setMeasurements(Measurements.AREA + Measurements.PERIMETER + Measurements.ELLIPSE + Measurements.SHAPE_DESCRIPTORS + Measurements.FERET + Measurements.CENTROID);
                /*
        	int measurements = Analyzer.getMeasurements(); // defined in Set Measurements dialog
		Analyzer.setMeasurements(0);
		measurements |= AREA+PERIMETER+ELLIPSE+SHAPE_DESCRIPTORS+FERET;
                measurements |= CENTROID; // make sure centroid is included
		measurements |= MIN_MAX;  // make sure min_max is included
		Analyzer.setMeasurements(measurements);
		Analyzer a = new Analyzer();
		ParticleAnalyzer pa = new ParticleAnalyzer();
                 */

                ResultsTable rt = new ResultsTable();
                //rt.reset();

                //rt.show("rt");
                /*
            RoiManager rm = RoiManager.getInstance();
            if (rm == null) {
                rm = new RoiManager();
            } else {
                rm.removeAll();
            }
            rm.setVisible(false);
            rm.setSize(0, 0);
            rm.setLocation(0, 0);
                 */
                rm = new RoiManager(false);

                //double tamaxgra = Interfazmac.areamax;
                double areamin = Double.parseDouble(Interfazmac.tfmin.getText());
                double areamax = Double.parseDouble(Interfazmac.tfmax.getText());

                ///MEDIDAS ES EN PIXELES, PARA PASAR A MM O CM MULTIPLICAR *Math.pow(pixpuni, 2)///
                int options = ParticleAnalyzer.EXCLUDE_EDGE_PARTICLES + ParticleAnalyzer.CLEAR_WORKSHEET;

                int measurements = Measurements.AREA + Measurements.PERIMETER + Measurements.ELLIPSE + Measurements.SHAPE_DESCRIPTORS + Measurements.CENTROID + Measurements.FERET;

                //ParticleAnalyzer pa = new ParticleAnalyzer(optionxs, measurements, rt, (areamin*Math.pow(pixpuni, 2)), (areamax*Math.pow(pixpuni, 2)), 0.40, 1.0);
                ParticleAnalyzer pa = new ParticleAnalyzer(options, measurements, rt, (areamin * Math.pow(pixpuni, 2)), (areamax * Math.pow(pixpuni, 2)), 0.40, 1.0);
                pa.setRoiManager(rm);
                rm.reset();
                pa.analyze(copia);

                //if ("cm".equals(unidadselect)) {areamin=areamin/100; areamax=areamax/100;} else {areamin=areamin; areamax=areamax;}
                //IJ.run(copia, "Analyze Particles...", "size=" + areamin + "-" + areamax + " circularity=0.40-1.00 exclude clear add");
                n = rt.getCounter();
                //rt.show("Results");
                //System.out.println("número de resultados:" + n);
                /*
            for (int id = 0; id < rt.getCounter(); id++) {
                double solidi = rt.getValue("Solidity", id);
            
            if (solidi < 0.85) {
                String[] options = {"Aceptar"};
                panes = new JOptionPane("¡¡No hay resultados!! No se dectectaron granos en la imagen."
                        + "\n El tamaño máximo y mínimo del grano puede requerir ajuste."
                        + "\n También puede ser necesario modificar el umbral de segmentación",
                JOptionPane.ERROR_MESSAGE, 0, icon2, options, options[0]);
                JDialog dialogs = panes.createDialog(f, "No hay resultados de salida");
                dialogs.setIconImage((icon).getImage());
                dialogs.setAlwaysOnTop(true);
                dialogs.setVisible(true);
                Interfazmac.statusBar.setText("El análisis no pudo continuar, revise el umbral o el tamaño del grano");
                return;
            }}
                 */
                if (n <= 0) {
                    String msg = "¡¡No hay resultados!! No se dectectaron granos en la imagen. El tamaño máximo y mínimo del grano puede requerir ajuste. También puede ser necesario modificar el umbral de segmentación";
                    panes = new JOptionPane(getMessage(msg, 400),
                            //panes = new JOptionPane("¡¡No hay resultados!! No se dectectaron granos en la imagen."
                            //+ "\n El tamaño máximo y mínimo del grano puede requerir ajuste."
                            //+ "\n También puede ser necesario modificar el umbral de segmentación",
                            JOptionPane.ERROR_MESSAGE, 0, icon2, new String[]{"Aceptar"}, "Aceptar");
                    JDialog dialogs = panes.createDialog(Interfazmac.myJFrame, "No hay resultados de salida");
                    dialogs.setIconImage((icon).getImage());
                    dialogs.setAlwaysOnTop(true);
                    dialogs.setVisible(true);
                    return;
                }

                // list = {AREA=0,MEAN=1,STD_DEV=2,MODE=3,MIN_MAX=4,
                //CENTROID=5,CENTER_OF_MASS,PERIMETER,RECT,ELLIPSE,SHAPE_DESCRIPTORS, FERET, 
                //INTEGRATED_DENSITY,MEDIAN,SKEWNESS,KURTOSIS,AREA_FRACTION,STACK_POSITION,
                //LIMIT,LABELS,INVERT_Y,SCIENTIFIC_NOTATION,ADD_TO_OVERLAY,NaN_EMPTY_CELLS};
                //los nombres de las variables se encuentran en ResultsTable
                //float[] area = rt.getColumn(ResultsTable.AREA);
                float[] area = rt.getColumn(0);
                float[] areas = new float[n];
                float[] perimetro = rt.getColumn(ResultsTable.PERIMETER);
                float[] circularidad = rt.getColumn(ResultsTable.CIRCULARITY);
                float[] largo = rt.getColumn(ResultsTable.FERET);
                float[] ancho = rt.getColumn(ResultsTable.MIN_FERET);
                float[] aspecto = rt.getColumn(ResultsTable.ASPECT_RATIO);
                float[] redondez = rt.getColumn(ResultsTable.ROUNDNESS);
                mart = calculateMean(area);
                float mcir = calculateMean(circularidad);
                dec = new DecimalFormat("0.000");
                List<Integer> grauni = new ArrayList<>();
                List<Float> gragv = new ArrayList<>();
                SimpleAttributeSet ITALIC_GRAY = new SimpleAttributeSet();
                for (int idx = 0; idx < rm.getCount(); idx++) {
                    if (rt.getValue("Area", idx) / mart >= 1.6) {
                        grauni.add(idx + 1);
                    }
                }
                rt.reset();

                if (grauni.size() == 1) {
                    appendText("El grano número " + grauni.toString() + " parece que son dos unidos. " + "\n", ITALIC_GRAY);
                }
                if (grauni.size() > 1) {
                    appendText("Los granos " + grauni.stream().map(String::valueOf).collect(Collectors.joining(", ")) + " parecen que son dos unidos. " + "\n", ITALIC_GRAY);
                }
                ////////////*Fin del analisis de imagen binarizada*//////////// 
                IJ.showProgress(6, 20);
                Interfazmac.progressBar.setValue(25);
                ////////////*Guardar imagen de salida*//////////// 
                //overlay = copia.getOverlay();
                overlay = new Overlay();

                /*
            if (Interfazmac.vpis.isSelected()) {
                //overlay.drawLabels(false);

                int index = 0;
                Roi[] rois = rm.getRoisAsArray();
                //index++;
                while (index < rois.length) {
                    
                    Roi roi = rois[index];
                    String[] pointxy = Arrays.toString(roi.getContainedPoints()).replaceAll("^\\s*\\[|\\]\\s*$", "").split("\\s*,\\s*");
                    String[] diamfere = Arrays.toString(roi.getFeretValues()).replaceAll("^\\s*\\[|\\]\\s*$", "").split("\\s*,\\s*");
                    double diam = Double.parseDouble(diamfere[0]);
                    ImageProcessor statip = imp.getProcessor();
                    statip.setRoi(roi);
                    ImageStatistics stats = statip.getStatistics();
                    
                    //System.out.println("x,y : " + stats.mean);
                   
                    // Obtiene "Feret" (maximum caliper width), "FeretAngle" and
                     //"MinFeret" (minimum caliper width), "FeretX" and
                     //"FeretY".
                     
                    //[0] = diameter; [1] = angle;	[2] = min; [3] = feretX; [4] = feretY;
                    String[] valuesferet = Arrays.toString(roi.getFeretValues()).replaceAll("^\\s*\\[|\\]\\s*$", "").split("\\s*,\\s*");
                    String[] valuesxy = Arrays.toString(roi.getContourCentroid()).replaceAll("^\\s*\\[|\\]\\s*$", "").split("\\s*,\\s*");
                    double xc = Double.parseDouble(valuesxy[0]);
                    double yc = Double.parseDouble(valuesxy[1]);
                    //double valmax = Double.parseDouble(valuesferet[0]); //Feret, Feretx, Ferety, feretangle, y feretmin 
                    double valmin = Double.parseDouble(valuesferet[2]);
                    double valmax = Double.parseDouble(Arrays.toString(roi.getFeretValues()).replaceAll("^\\s*\\[|\\]\\s*$", "").split("\\s*,\\s*")[0]); //Feret, Feretx, Ferety, feretangle, y feretmin 
                    //System.out.println(" Largo :"+ (valmin/ pixpuni)+ "Ancho :"+valmax/ pixpuni );
                    //System.out.println("x,y : "+Arrays.toString(roisxy));
                    //Roi roi = rm.getRoi(index);   
                    double roiarea = rt.getValue("Area", index);
                    if (unidadselect.equals("cm")) {
                       roiarea = roiarea*100;
                    }
                    roi.setFillColor(makeLut((int) Math.round((roiarea))));
                    overlay.add(roi);
                    Roi roi2 = roi;
                    roi2 = new ShapeRoi(roi2);
                    roi2.setStrokeWidth(2);
                    roi2.setStrokeColor(Color.white);
                    overlay.add(roi2);
                    //System.out.println("x : "+ x + "y : "+ y);
                    TextRoi roiet = new TextRoi(xc, (yc) - 14, Integer.toString(index + 1), new Font("Dialog", Font.PLAIN, 22));
                    roiet.setStrokeColor(Color.blue);
                    roiet.setJustification(1);
                    overlay.add(roiet);
                    index++;
                }

                imp2 = imp.duplicate(); //se ocupa duplicar la imagen original antes de convertirla en stack
                imp2.setOverlay(overlay);
		imp2.updateAndDraw();
            }
                 */
                if (Interfazmac.guarda_imas.isSelected() || Interfazmac.vpis.isSelected()) {
                    //CREATE SELECTION
                    //ThresholdToSelection tts = new ThresholdToSelection();
                    //Roi roim = (new ThresholdToSelection()).convert(copia.getProcessor());
                    Roi roim = Roi.xor(rm.getSelectedRoisAsArray());
                    colorearim = imp.duplicate();
                    //colorearim.setTitle("Colorear");
                    colorearim.getProcessor().setColor(fondopanel);
                    colorearim.getProcessor().fillOutside(roim);
                    /*
        for (int ix = 0; ix < rm.getCount(); ix++) {
        rm.rename(ix, ""+(ix+1));}
                     */
                    //no da formato a las etiquetas
                    //overlay.drawLabels(false);
                    //overlay.setStrokeColor(Color.white);
                    //overlay.setStrokeWidth(1.5);
                    //imp2 = imp.duplicate();
                    //imp2.setOverlay(overlay); //se sustituyó con roi2 = new ShapeRoi(roi2);
                    //imp2 = imp2.flatten();
                    for (int idx = 0; idx < rm.getCount(); idx++) {

                        //overlay.drawNames(Prefs.useNamesAsLabels);
                        //overlay.drawNames(overlay.getDrawNames());
                        //overlay.drawBackgrounds(true);
                        //overlay.setLabelColor(Color.blue);
                        //overlay.setLabelFont(new Font("Monospaced", Font.BOLD, 20));
                        //overlay.drawLabels(true);
                        //feretip.setRoi(roi);
                        //OBTENER EJES ANCHO Y LARGO
                        Roi roi = rm.getRoi(idx);
                        roi.setName(Integer.toString(idx + 1));
                        double[] valuesxy = roi.getContourCentroid();
                        double x = valuesxy[0]; //Feret, Feretx, Ferety, feretangle, y feretmin  
                        double y = valuesxy[1];
                        double[] valuesfe = roi.getFeretValues();
                        double a = valuesfe[0];
                        double angle = valuesfe[1];
                        double b = valuesfe[2];
                        double lxi = valuesfe[3]; //coordenadas X (3) e Y (4) del punto inicial del eje mayor
                        double lyi = valuesfe[4];
                        double lxf = valuesfe[10]; // coordenadas X (10) e Y (11) del punto final de ancho máximo
                        double lyf = valuesfe[11];
                        double axi = valuesfe[12]; //coordenadas X (12) e Y (13) del punto inicial del eje menor
                        double ayi = valuesfe[13];
                        double axf = valuesfe[14]; //coordenadas X (14) e Y (15) del punto final del eje menor
                        double ayf = valuesfe[15];
                        double roiarea = area[idx];

                        //rt.getValue("Area", idx);
                        //double roiarea = roi.getStatistics().area;
                        //double x = rt.getValue("X", idx);
                        //double x = rt.getValue("FeretX", idx); 
                        //double y = rt.getValue("Y", idx);
                        //double y = rt.getValue("FeretY", idx);
                        //double a = rt.getValue("Major", idx);
                        //double a = rt.getValue("Feret", idx);
                        //double b = rt.getValue("Minor", idx);
                        //double b = rt.getValue("MinFeret", idx);
                        //double angle = rt.getValue("Angle", idx);
                        //double angle = rt.getValue("FeretAngle", idx);
                        //System.out.println("X :"+ (rt.getValue("X", idx)/ ancpix)+ " FeretX :"+ rt.getValue("FeretX", idx));
                        //System.out.println("angle :"+ rt.getValue("Angle", idx)+ " FeretAngle :"+ rt.getValue("FeretAngle", idx));
                        //System.out.println("minor :"+ rt.getValue("Minor", idx)+ " MinFerete :"+ rt.getValue("MinFeret", idx));
                        double dx = a * Math.cos(angle / 180.0 * Math.PI) / 2.0;
                        double dy = -a * Math.sin(angle / 180.0 * Math.PI) / 2.0;
                        double x1 = x - dx;
                        double x2 = x + dx;
                        double y1 = y - dy;
                        double y2 = y + dy;
                        double aspectRatio = b / a;
                        //con feret no se ocupa dividir por el ancpix
                        /*cordenadas para largo*/

                        double ax1 = x1;
                        double ay1 = y1;
                        double ax2 = x2;
                        double ay2 = y2;

                        //double elimaj = a / ancpix;
                        //double elimin = b / ancpix;
                        //double maymin = elimaj / elimin;
                        /*
                double ax1 = x1;
                double ay1 = y1;
                double ax2 = x2;
                double ay2 = y2;
                System.out.println("x1 :" + (rt.getValue("X", idx) - dx) / ancpix+ 
                        "y1 :" + (rt.getValue("Y", idx) - dy) / ancpix+
                        "x2 :" + (rt.getValue("X", idx) + dx) / ancpix+ 
                        "y2 :" + (rt.getValue("Y", idx) + dy) / ancpix+
                        " x1 :" + x1 + "y1 :" + y1 + " x2 :" + x + "y2 :" + y);
                         */
                        // Roi roi2 = new EllipseRoi(x1,y1,x2,y2,aspectRatio);
                        //feretip.drawLine((int)ax1,(int)ay1,(int)ax2,(int)ay2);
                        //feretip.setLineWidth(5);
                        //feretip.setColor(Color.yellow);
                        //feretip.draw(linemajor);

                        /*cordenadas para ancho*/
                        double ang = angle * (Math.PI / 180) + (Math.PI / 2);
                        double bx1 = (x + (b / 2) * Math.cos(ang));
                        double by1 = (y - (b / 2) * Math.sin(ang));
                        double bx2 = (x - (b / 2) * Math.cos(ang));
                        double by2 = (y + (b / 2) * Math.sin(ang));
                        //feretip.drawLine((int)bx1,(int)by1,(int)bx2,(int)by2);
                        /*
                double bx1 = (x + (b / 2) * Math.cos(ang));
                double by1 = (y - (b / 2) * Math.sin(ang));
                double bx2 = (x - (b / 2) * Math.cos(ang));
                double by2 = (y + (b / 2) * Math.sin(ang));
                //System.out.println("Y :" + (rt.getValue("Y", idx) + (b / 2) * Math.sin(ang)) / ancpix + " FeretX :" + (rt.getValue("FeretY", idx) + (b / 2) * Math.sin(ang)));
                         */
 /*agregar objetos identificados a la mascara*/

                        //roi.setFillColor(getMaskColor(Math.round(idx-(idx/8))));
                        if (unidadselect.equals("cm")) {
                            roiarea = roiarea * 100;
                        }
                        //roi.setFillColor(getMaskColor((int)(roiarea)));
                        //roi.setFillColor(getMaskColor((int) Math.round((roiarea))));

                        if (roiarea / mart >= 1.6) {
                            roi.setFillColor(new Color(255, 255, 0, 100));
                            roi.clone();
                            //Interfazmac.logres.append("El grano número "+(idx+1)+" parece que son dos unidos"+ "\n");
                            //String newLine = System.getProperty("line.separator"); 
                            //Interfazmac.textPane1.setText("El grano número "+(idx+1)+" parece que son dos unidos"+ newLine);

                        } else {
                            roi.setFillColor(makeLut((int) Math.round((roiarea))));
                        }

                        //roi.setFillColor(new Color(255, 0, 0, 80));
                        //System.out.println("Tamaño integro :" + (int) Math.round((roiarea)));
                        //roi.setFillColor(getMaskColor((int)(roiarea)- (int)(roiarea/5)));
                        /*colorear los objetos identificados de acuerdo a su tamaño*/
                        overlay.add(roi);
                        /*
                    
                    if (unidadselect == "mm") {
                        if (roiarea > 23.1) {
                            roi.setFillColor(new Color(255, 0, 0, 80));
                            //roi.setFillColor(getMaskColor(idx-1));
                        }
                        if (roiarea <= 23.1) {
                            roi.setFillColor(new Color(110, 40, 20, 70));
                        }
                        if (roiarea <= 14.1) {
                            roi.setFillColor(new Color(200, 150, 0, 70));
                        }
                        if (roiarea < 7) {
                            roi.setFillColor(new Color(0, 0, 255, 70));
                        }
                        if (roiarea <= 14.1 && roifer <= 4.6) {
                            roi.setFillColor(new Color(255, 255, 0, 85));
                        }
                    } else {
                        if (roiarea > .231) {
                            roi.setFillColor(new Color(255, 0, 0, 80));
                        }
                        if (roiarea <= 0.231) {
                            roi.setFillColor(new Color(110, 40, 20, 70));
                        }
                        if (roiarea <= 0.141) {
                            roi.setFillColor(new Color(200, 150, 0, 70));
                        }
                        if (roiarea < 0.07) {
                            roi.setFillColor(new Color(0, 0, 255, 70));
                        }
                        if (roiarea <= 0.141 && roifer <= 0.46) {
                            roi.setFillColor(new Color(255, 255, 0, 85));
                        }
                    }
                         */
                        //Roi roi2 = roi;
                        roi = new ShapeRoi(roi);
                        roi.setStrokeWidth(1.2);
                        roi.setStrokeColor(Color.BLUE);
                        colorearim.getProcessor().setColor(Color.BLUE);
                        colorearim.getProcessor().setLineWidth(2);
                        new ShapeRoi(roi).drawPixels(colorearim.getProcessor());
                        overlay.add(roi);
                        /*agregar ejes y etiquetas a los objetos identificados a la mascara*/
                        //Roi linelargo = new Line(ax1, ay1, ax2, ay2);
                        Roi linelargo = new Line(lxi, lyi, lxf, lyf);
                        linelargo.setStrokeColor(Color.yellow);
                        linelargo.setStrokeWidth(2);
                        colorearim.getProcessor().setColor(Color.YELLOW);
                        colorearim.getProcessor().drawLine((int) lxi, (int) lyi, (int) lxf, (int) lyf);
                        overlay.add(linelargo);

                        Roi lineancho = new Line(bx1, by1, bx2, by2);
                        //Roi lineancho = new Line(axi, ayi, axf, ayf);
                        lineancho.setStrokeColor(Color.green);
                        lineancho.setStrokeWidth(2);
                        colorearim.getProcessor().setColor(Color.GREEN);
                        colorearim.getProcessor().drawLine((int) bx1, (int) by1, (int) bx2, (int) by2);
                        overlay.add(lineancho);

                        TextRoi roilab = new TextRoi(x, y - 14, Integer.toString(idx + 1), new Font("SansSerif", Font.PLAIN, 22));
                        roilab.setJustification(1);
                        overlay.add(roilab);
                        //System.out.println("x:"+overlay.size());
                        //feretip.draw(lineminor);

                        //ESTABLECER BARRA ESACALA
                        Roi hBarRoi = new Roi(colorearim.getWidth() - 150, colorearim.getHeight() - 80, pixpuni * 10, 10);
                        hBarRoi.setFillColor(Color.red);
                        overlay.add(hBarRoi);
                        int escal = (unidadselect.equals("mm")) ? 10 : 1;
                        TextRoi roisca = new TextRoi(colorearim.getWidth() - 145, colorearim.getHeight() - 70, Integer.toString(escal) + " " + unidadselect, new Font("SansSerif", Font.PLAIN, 32));
                        roisca.setStrokeColor(Color.red);
                        //roisca.setJustification(1);
                        overlay.add(roisca);
                        //ESTABLECER REFERENCIA NORTE
                        TextRoi roinorte = new TextRoi(colorearim.getWidth() - 70, 10, "N", new Font("SansSerif", Font.PLAIN, 32));
                        roinorte.setStrokeColor(Color.red);
                        overlay.add(roinorte);
                        Arrow arrow = new Arrow(colorearim.getWidth() - 60, 100, colorearim.getWidth() - 60, 50);
                        //roima.setStrokeWidth((float)9.0);
                        arrow.setStrokeColor(Color.red);
                        arrow.setFillColor(Color.red);
                        arrow.setStrokeWidth(7.0);
                        arrow.setHeadSize(16.0);
                        arrow.setStyle(1);
                        arrow.setOutline(false);
                        arrow.setDoubleHeaded(false);
                        overlay.add(arrow);

                    }

                    FontMetrics fm = colorearim.getProcessor().getFontMetrics();
                    colorearim.getProcessor().setFont(new Font("Monospaced", Font.BOLD, 24));
                    //colorearim.getProcessor().setColor(Color.white);
                    int fontHeight = fm.getHeight();
                    int tw = fm.stringWidth("ANALIZADA EN:" + etiqueta);
                    //int stringHeight = fm.getHeight();

                    colorearim.getProcessor().drawString("IMAGEN ANALIZADA EN:" + etiqueta + ", UMBRAL:" + umbralseg + "\nTAMAÑO DE GRANO MINIMO:" + Interfazmac.tfmin.getText() + ", MAXIMO:" + Interfazmac.tfmax.getText(), 40, 40);
                    //feretip.drawString("ANALIZADA EN:"+etiqueta+"\n UMBRAL:"+umbralseg+"\n TAMAÑO DE GRANO MINIMO :"+Interfazmac.tfmin.getText()+"\n TAMAÑO DE GRANO MAXIMO :"+Interfazmac.tfmax.getText(), (feretip.getWidth()/2) - (tw/2), 120);

                    //no redibuja las etiquetas
                    rm.setEditMode(colorearim, noUpdateMode);
                    colorearim.setOverlay(overlay);

                    //colorearim.updateAndDraw();
                    // IJ.run(colorearim, "From ROI Manager", "");
                    //colorearim.setOverlay(overlay);
                    //colorearim = imp.flatten();
                    if (Interfazmac.guarda_imas.isSelected()) {
                        //boolean success = new File(dir + this.PROCESADAS).mkdir();
                        File Procesadas = new File(dir + "Procesadas" + File.separator);
                        if (!Procesadas.exists()) {
                            Procesadas.mkdirs();
                        }
                        JpegWriter.save(colorearim, dir + "/Procesadas/" + lbl + "_" + ext + etiqueta + " Umbral " + umbralseg + " " + labelumbral + ".jpg", JpegWriter.DEFAULT_QUALITY);
                        //IJ.saveAs(colorearim, "Jpeg", dir + "/Procesadas/" + lbl + "_" + ext + etiqueta + " Umbral " + umbralseg);
                        //colorearim.close();
                    }
                    if (Interfazmac.vpis.isSelected()) {

                        if (WindowManager.getWindow("Imagen " + lbl + " segmentada con umbral " + umbralseg + " " + labelumbral) != null) {
                            colorearim.changes = false;
                            colorearim.close();

                        } else {
                            colorearim.setTitle("Imagen " + lbl + " segmentada con umbral " + umbralseg + " " + labelumbral);
                            //copia.setTitle("Hojas identificadas en la imagen "+lbl);
                            //colorearim.show();
                            MenuBar menuBarc = new MenuBar();
                            menuBarc.setFont(new Font("Dialog", Font.BOLD, 12));
                            Menu archivogc = new Menu("Archivo");
                            //guardimc = new MenuItem("Guardar");
                            //guardimc.setLabel("Guardar");
                            archivogc.add(Interfazmac.guardimc);
                            //guardimc.addActionListener(this);
                            menuBarc.add(archivogc);
                            //imp2.show();
                            /*
                        ImageWindow winc = new ImageWindow(colorearim);
                        winc.addMouseListener(this);
                        //win.setBounds(0, 0, anchuroi, alturaroi);
                        winc.setIconImage(((icon).getImage()));
                        winc.setMenuBar(menuBarc);
                        winc.setBackground(fondopanel);
                        //win.setLocationAndSize(0, 0, anchuroi, alturaroi);
                        int xrh = copia.width;
                        int yrh = copia.height;
                        winc.getCanvas().zoomOut(xrh, yrh);
                        winc.setLocationRelativeTo(Interfazmac.myJFrame);
                             */
                            //Interfazmac.myJFrame.add(win);
                            //winc.setVisible(true);  
                            Toolbar.getInstance().setTool(Toolbar.HAND);
                            //copia.getWindow().setVisible(false);
                            //Fco.setExtendedState(Frame.ICONIFIED);
                        }

                    }

                    imp.changes = false;

                    ////////////*fin del guardado de la imagen de salida*//////////// 
                    if (Interfazmac.vpis.isSelected()) {

                        Roi[] rois = rm.getRoisAsArray();
                        /*
rm.reset();
int count =0;
       for (Roi roi : rois) {
              // Obtener el centro de rotación
double centerX = roi.getBounds().width/ 2.0;
double centerY = roi.getBounds().height / 2.0;
                    double[] valuesfe = roi.getFeretValues();
                    double angle = valuesfe[1];
  
            double radians = Math.toRadians(angle); 
    // Obtener los puntos de la ROI
    FloatPolygon polygon = roi.getFloatPolygon();
    float[] xpoints = polygon.xpoints;
    float[] ypoints = polygon.ypoints;
    int npoints = polygon.npoints;

    // Crear una matriz de transformación de rotación
    AffineTransform transform = AffineTransform.getRotateInstance(radians, centerX, centerY);

    // Aplicar la transformación a cada punto de la ROI
    float[] xpointsRotated = new float[npoints];
    float[] ypointsRotated = new float[npoints];
    for (int i = 0; i < npoints; i++) {
        transform.transform(new double[]{xpoints[i], ypoints[i]}, 0, new double[]{xpointsRotated[i], ypointsRotated[i]}, 0, 1);
    }

    // Crear una nueva ROI con las coordenadas rotadas
    FloatPolygon rotatedPolygon = new FloatPolygon(xpointsRotated, ypointsRotated, npoints);
   PolygonRoi rotatedRoi = new PolygonRoi(rotatedPolygon, roi.getType());
System.out.println("cuál es el angulo "+ Float.toString(xpointsRotated[count]));
rm.add(rotatedRoi, count);
count++;
} 
                         */
                        int w = 0, h = 0, x = 0, y = 0, filasgc = (n > (n / 5) * 5) ? 6 : 5;
                        int altos = 0, anchos = 0;
                        for (int i = 0; i < rm.getCount(); i++) {
                            //rm.rename(i, String.format("%09.3f", area[i])); 
                            if (rois[i].getBounds().width > w) {
                                w = rois[i].getBounds().width;
                            }
                            if (rois[i].getBounds().height > h) {
                                h = rois[i].getBounds().height;
                            }
                            rois[i].setName(String.format("%09.1f", area[i]));
                        }

                        // Ordenar los ROIs por nombre sin rm.runCommand("Sort");
                        //Arrays.sort(rois, Comparator.comparing(Roi::getName));
                        Arrays.sort(rois, Comparator.comparing(Roi::getName));
                        rm.reset();

                        // Agregar los ROIs ordenados nuevamente al ROI Manager
                        for (Roi roi : rois) {
                            rm.addRoi(roi);

                        }

                        int anchfin = (colorearim.getWidth() / w) * w;
                        int altofin = ((rm.getCount() / ((int) colorearim.getWidth() / w)) * (int) (colorearim.getWidth() / w) < rm.getCount()) ? (rm.getCount() / (colorearim.getWidth() / w) * h) + h + 10 : ((rm.getCount() / (colorearim.getWidth() / w)) * h) + 10;

                        ImagePlus[] images = colorearim.crop(rm.getSelectedRoisAsArray());
                        //new ColorProcessor(colorearim.width, colorearim.height)
                        //new ColorProcessor(w*(n/5), (h*filasgc));
                        ImageProcessor ip2 = new ColorProcessor(anchfin, altofin);
                        ip2.setColor(Color.WHITE);
                        ip2.fill();
                        ip2.setFont(new Font("SansSerif", Font.PLAIN, 28));
                        ip2.setColor(Color.BLACK);
                        ImagePlus imp2 = new ImagePlus("Montage", ip2);
                        ip2 = imp2.getProcessor();
                        Double screenSize = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
                        //int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
                        for (int i = 0; i < images.length; i++) {
                            //System.out.println(sortedNames[i]);
                            //ImageProcessor ip2 = new ColorProcessor(w, h);
                            //int xoff=0, yoff=0;
                            //anchuroi += (w-images[i].getProcessor().getWidth());
                            //alturaroi += (h-images[i].getProcessor().getHeight());
                            //xoff = (w-images[i].getProcessor().getWidth())/2;
                            //yoff = (h-images[i].getProcessor().getHeight())/2;
                            //new Wand(images[i].getProcessor()).autoOutline(xoff, yoff, images[i].getProcessor().getMaxThreshold(), Wand.LEGACY_MODE);

                            x = (anchos + ((w - rois[i].getBounds().width) / 2));
                            y = (altos + ((h - rois[i].getBounds().height) / 2));

                            Roi roi = images[i].getRoi();
                            images[i].getProcessor().setColor(Color.WHITE);
                            images[i].getProcessor().fillOutside(roi);
                            images[i].getProcessor().setColor(Color.BLUE);
                            images[i].getProcessor().setLineWidth(2);
                            new ShapeRoi(roi).drawPixels(images[i].getProcessor());
                            ip2.insert(images[i].getProcessor(), x, y);
                            ip2.drawString(Integer.toString(i + 1), anchos + (w / 2) - 20, altos + (h / 2) + 5);
                            ip2.drawString(Double.toString(Double.parseDouble(rm.getName(i))), anchos + (w / 2) - 20, altos + (h) + 15);

                            anchos += w;

                            if (anchos + w >= (colorearim.getWidth())) {
                                anchos = 0;
                                //anchfin += w;	no se cummple con una sola fila		 
                                altos += h;
                                if (altos >= colorearim.getHeight()) {
                                    break;
                                }
                            }

                        }
                        System.out.println("ancho final " + Integer.toString(anchfin) + " alto final " + Integer.toString(altofin));
                        if (WindowManager.getWindow("Granos identificados en la imagen " + lbl) != null) {
                            imp2.changes = false;
                            imp2.close();

                        } else {

                            imp2.setTitle("Granos identificados en la imagen " + lbl);
                            imp2.updateAndDraw();

                            Interfazmac.panelima.clearImage();

                            imp2.setProcessor(null, ip2);

                            bufferedImage = imp2.getBufferedImage();

                            System.out.println("ancho final " + Integer.toString(Interfazmac.panelima.getPreferredSize().height));
                            // Escalar la imagen (puedes ajustar el factor de escala según sea necesario)
                            double scale = 0.5; // Factor de escala (por ejemplo, 0.5 para reducir a la mitad)
                            int scaledWidth = (int) (bufferedImage.getWidth() * scale);
                            int scaledHeight = (int) (bufferedImage.getHeight() * scale);
                            Image scaledImage = bufferedImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);

                            Interfazmac.panelima.setImage(bufferedImage);
                            //Interfazmac.panelima.setBackground(Color.GREEN);
                            //Interfazmac.panelima.repaint();

                        }
                    }
                }
                IJ.showProgress(7, 20);
                Interfazmac.progressBar.setValue(30);
                /*
	if (guarda_ima==true) {
	//boolean success = new File(dir + this.PROCESADAS).mkdir();
	File Procesadas = new File(dir+"Procesadas"+File.separator);
       	if (!Procesadas.exists()) {
            Procesadas.mkdirs();
        	}
	ImagePlus colorear= imp.duplicate();
	IJ.run(colorear, "Canvas Size...", "width=" +anchura+2+ " height=" +altura+2+ " position=Center");
	colorear.show();
	IJ.run("Set... ", "zoom=1 x=-1000 y=-1000");
	//createEllipse(colorear);
	//IJ.run(colorear, "roicolor", "");
	IJ.runMacroFile("ij.jar:roicolor", null);
	//IJ.run("From ROI Manager", "");
	//IJ.saveAs(imp, "Jpeg", "name");
	IJ.saveAs(colorear, "Jpeg", dir+"/Procesadas/"+lbl+"_"+ext+etiqueta+" procesada.jpg");
	colorear.close();
	//imp.close();
	}
                 */

                //Borrar columnas y renombrar encabezados
                //rt.showRowNumbers(true); 
                //rt.getFreeColumn("Número");
                //rt.deleteColumn("X");
                //rt.deleteColumn("Y");
                //rt.renameColumn("Index", "año");
                //rt.renameColumn("Area", "Tamaño");
                //imp.show();
                /* Obtener color*/
                //int nSlices = imp.getNSlices();
                // ImagePlus imp = IJ.getImage();
                if (imp.getBitDepth() != 24) {
                    String[] buttons = {"Convertir", "Cancelar"};
                    String msg = "La imagen " + imp.getTitle() + " requiere convertirse a 24 bits para continuar "
                            + "con el análisis de color, pero al realizar dicha conversión debe considerar irrelevante "
                            + "la información obtenida sobre el color.";
                    //panes = new JOptionPane("La imagen "+ imp.getTitle() +" requiere convertirse a 24 bits para     \n"
                    //        + "continuar con el análisis de color, pero al realizar dicha conversión debe\n"
                    //        + "considerar irrelevante la información obtenida sobre el color.",
                    panes = new JOptionPane(getMessage(msg, 400),
                            JOptionPane.WARNING_MESSAGE, 0, icon1, buttons, buttons[0]);
                    JDialog dialogs = panes.createDialog(Interfazmac.myJFrame, "Error la imagen tiene un canal único");
                    dialogs.setIconImage((icon).getImage());
                    dialogs.setAlwaysOnTop(true);
                    dialogs.setVisible(true);

                    if (panes.getValue() == null) {
                        IJ.showStatus("El análisis del archivo: " + imp.getTitle() + " se detuvo");
                        Interfazmac.statusBar.setText("El análisis del archivo: " + imp.getTitle() + " se detuvo");
                        return;
                    } else if (panes.getValue().equals("Cancelar")) {
                        IJ.showStatus("El análisis del archivo: " + imp.getTitle() + " se detuvo");
                        Interfazmac.statusBar.setText("El análisis del archivo: " + imp.getTitle() + " se detuvo");
                        return;
                    } else if (panes.getValue().equals("Convertir")) {
                        imp.unlock();
                        new ImageConverter(imp).convertToRGB();
                    }
                }

                switch (etiqueta) {
                    case "RGB":
                        Interfazmac.progressBar.setValue(35);
                        et1 = "Rojo";
                        et2 = "Verde";
                        et3 = "Azul";
                        et4 = "Intensidad";
                        //imp = CompositeConverter.makeComposite(imp);
                        /*
               	int width = imp.getWidth();
		int height = imp.getHeight();
		ImageStack stack1 = imp.getStack();
		int n = stack1.getSize();
		ImageStack stack = new ImageStack(width, height);
		for (int i=0; i<n; i++) {
		ColorProcessor ip = (ColorProcessor)stack1.getProcessor(1);
		stack1.deleteSlice(1);
		byte[] R = new byte[width*height];
		byte[] G = new byte[width*height];
		byte[] B = new byte[width*height];
		ip.getRGB(R, G, B);
		stack.addSlice(null, R);
		stack.addSlice(null, G);
		stack.addSlice(null, B);
		}
		n *= 3;
		imp = new ImagePlus(imp.getTitle(), stack);
		imp.setDimensions(3, n/3, 1);
                         */
                        //imp.hide(); IJ.run("Set... ", "zoom=5 x=0 y=0");	

                        //Color_Transformer tranco= new Color_Transformer();
                        //tranco.getLab();
                        break;

                    case "Lab":
                        Interfazmac.progressBar.setValue(40);
                        et1 = "L (Luminancia)";
                        et2 = "a (+rojo-verde)";
                        et3 = "b (+amarillo-azul)";
                        et4 = "Intensidad";
                        //ColorSpaceConverter converter = new ColorSpaceConverter();
                        //imp= converter.RGBToLab(imp);
                        //imp = new ColorSpaceConverter().RGBToLab(imp);
                        /*
		//ColorProcessor cp = (ColorProcessor)imp.getProcessor();
		ColorSpaceConverter converter = new ColorSpaceConverter();
		int[] pixels = (int[])((ColorProcessor)imp.getProcessor()).getPixels();
               	int width = imp.getWidth();
		int height = imp.getHeight();
		ImageStack stack = new ImageStack(width, height);
		FloatProcessor L = new FloatProcessor(width, height);
		FloatProcessor a = new FloatProcessor(width, height);
		FloatProcessor b = new FloatProcessor(width, height);
		stack.addSlice("L*",L);
		stack.addSlice("a*",a);
		stack.addSlice("b*",b);
		for (int i=0; i<pixels.length; i++) {
			double[] values = converter.RGBtoLAB(pixels[i]);
			L.setf(i,(float)values[0]);
			a.setf(i,(float)values[1]);
			b.setf(i,(float)values[2]);
		}
		imp = new ImagePlus(imp.getTitle(),stack);
                         */
                        //La opción instalando el plugin color transformer
                        //IJ.run(imp, "Color Transformer", "colour=Lab");
                        //Color_Transformer tci = new Color_Transformer();
                        //tci.setup("Lab", imp);
                        //tci.equals("Lab");
                        //imp = IJ.getImage(); imp.getWindow().setVisible(false); 
                        break;
                    case "HSB":
                        Interfazmac.progressBar.setValue(50);
                        et1 = "Tono";
                        et2 = "Saturación";
                        et3 = "Brillo";
                        et4 = "Intensidad";
                }
                //IJ.run("Set... ", "zoom=5 x=0 y=0");	
                //imp = WindowManager.getCurrentImage();
                /*
        int index = 0;
        Roi[] rois = rm.getRoisAsArray();
        while (index < rois.length) {

            Roi roi = rois[index];
            imp.setRoi(roi);
            for (int i = 1; i <= imp.getStackSize(); i++) {
                double rojo = imp.getStack().getProcessor(1).getStatistics().mean;
                double verde = imp.getStack().getProcessor(2).getStatistics().mean;
                double azul = imp.getStack().getProcessor(3).getStatistics().mean;
                System.out.println("Color rojo : " + rojo + "Color verde : " + verde + "Color azul : " + azul);
            }
            index++;
        }
                 */

//Esto permite medir el color de manera correcta cuando hay agujeros al interior del limbo
                float[] rojo = new float[n];
                float[] verde = new float[n];
                float[] azul = new float[n];
                float[] tono = new float[n];
                float[] satura = new float[n];
                float[] brillo = new float[n];

                float[][] valcol = new ConvertColor().medircolor(imp, rm, etiqueta, umbralseg);
                float[][] valhsb = new ConvertColor().getHSV(imp, rm, umbralseg);

                for (int i = 0; i < rm.getCount(); i++) {
                    rojo[i] = valcol[0][i];
                    verde[i] = valcol[1][i];
                    azul[i] = valcol[2][i];
                    tono[i] = valhsb[0][i];
                    satura[i] = valhsb[1][i];
                    brillo[i] = valhsb[2][i];

                }

                /*    
            Analyzer.setMeasurements(Measurements.MEAN);
            ResultsTable rt1 = rm.multiMeasure(imp);
            String[] hdr = rt1.getHeadings();

            if (rt1 == null) {
                throw new NullPointerException();
            }
            ResultsTable rtcolor = new ResultsTable();
            double[] valc = new double[rt1.getCounter()];
            //rt1.show("Color");
            String[] rgbs = {"red", "green", "blue"};

            for (int i = 0; i < rt1.getCounter(); i++) {
                for (int j = 0; j < hdr.length; j++) {
                    if (i == 0) {
                        rtcolor.setValue("Observaciones", j, j + 1);
                    }
                    //String  value = rt1.getStringValue(hdr[j], i);
                    valc[i] = rt1.getValue(hdr[j], i);
                    rtcolor.setValue(rgbs[i], j, valc[i]);
                }
            }
            rtcolor.getResultsTable();
            float[] rojo = rtcolor.getColumn(1);
            float[] verde = rtcolor.getColumn(2);
            float[] azul = rtcolor.getColumn(3);
                 */
                imp.changes = false;
                //imp.close();
                IJ.showProgress(9, 20);
                Interfazmac.progressBar.setValue(60);
                /*
	IJ.run("Stack to Images", "");
	IJ.run("Set... ", "zoom=5 x=-10 y=-10");
	//IJ.runMacro("roiManager('multi-measure measure_all');");
	IJ.runMacro("selectWindow('L'); n = nResults; rojo = newArray(n); for (i=0; i<n; i++) { roiManager('select', i); List.setMeasurements; rojo[i]  = List.getValue('Mean');} selectWindow('L'); close();");
	IJ.runMacro("selectWindow('a'); n = nResults; verde = newArray(n); for (i=0; i<n; i++) { roiManager('select', i); List.setMeasurements; verde[i]  = List.getValue('Mean');} selectWindow('a'); close();");
	IJ.runMacro("selectWindow('b'); n = nResults; azul = newArray(n); for (i=0; i<n; i++) { roiManager('select', i); List.setMeasurements; azul[i]  = List.getValue('Mean'); print(azul[i]);} selectWindow('b'); close();");
	//ResultsTable rt = ResultsTable.getResultsTable(); 
	float[] rojo= rt.getColumn(ResultsTable.MEAN);
	//IJ.selectWindow("Results"); 
	IJ.runMacro("selectWindow('a'); roiManager('multi-measure'); selectWindow('a'); close();");
	float[] verde=rt.getColumn(ResultsTable.MEAN);
	//IJ.selectWindow("Results"); 
	IJ.runMacro("selectWindow('b'); roiManager('multi-measure'); selectWindow('b'); close();");
	float[] azul = rt.getColumn(ResultsTable.MEAN);
	//IJ.selectWindow("Results"); 
	IJ.selectWindow("Results horribles"); 
	IJ.run("Close"); 
                 */
                //waitForUserDialog = new WaitForUserDialog("Comprobar proceso", "Espero te funcione.");
                //waitForUserDialog.show(); 
                /*
	imp.setSlice(3);
	imp = WindowManager.getCurrentImage();
	Resultados = rm.multiMeasure(imp);
	float[] azul = Resultados.getColumn(Resultados.MEAN);
	float[] azul = rt.getColumn(ResultsTable.MEAN);
	IJ.wait(50);
	IJ.run("Clear Results");
                 */
 /*
	IJ.run("Stack to Images", "setSlice(3)");
	rm.runCommand(imp,"Deselect");
	rm.runCommand(imp,"Measure");
	float[] azul= rt2.getColumn(rt2.MEAN);
	//rt2.reset();
	IJ.run("Clear Results");
	imp = WindowManager.getCurrentImage();
	imp.close();
	imp = WindowManager.getCurrentImage();
	//rm.runCommand(imp,"Deselect");
	rm.runCommand(imp,"Measure");
	float[] verde= rt2.getColumn(rt2.MEAN);
	//rt2.reset();
	IJ.run("Clear Results");
	imp.close();
	imp = WindowManager.getCurrentImage();
	//rm.runCommand(imp,"Deselect");
	rm.runCommand(imp,"Measure");
	float[] rojo= rt2.getColumn(rt2.MEAN);
	//rt2.reset();
	IJ.selectWindow("Results"); 
	IJ.run("Close"); 
	imp.close();
	//rt2.show("color");
	//showResultsWindow = false;
                 */
                //IJ.runMacro("if (isOpen('Results')) {selectWindow('Results'); run('Close');}"); 
                /* finaliza obtener color*/
                DateFormat dateFormat = new SimpleDateFormat("EEEE dd MMM yyyy");
                DateFormat horaFormat = new SimpleDateFormat("HH:mm:ss");
                String date = dateFormat.format(new java.util.Date());
                String hora = horaFormat.format(new java.util.Date());
                /*
		
	//crear tabla de resultados con ResultsTable
	//if (etiqueta==" RGB") {et1="Rojo"; et2="Verde"; et3="Azul"; et4="RGB/3";}
    	ResultsTable mfrt = new ResultsTable();
	int etiquetaindex = mfrt.getFreeColumn("Muestra: " +lbl);
	int countindex = mfrt.getFreeColumn("Número");
	int tamañoindex = mfrt.getFreeColumn("Tamaño ("+unidadc+")");
	int tsindex = mfrt.getFreeColumn("Tamaño Ts ("+unidadc+")");   
	int artgindex = mfrt.getFreeColumn("Tamaño Total ("+unidadc+")");   
	int largoindex = mfrt.getFreeColumn("Largo ("+unidad+")"); 
	int anchoindex = mfrt.getFreeColumn("Ancho ("+unidad+")"); 
	int perimetroindex = mfrt.getFreeColumn("Perímetro ("+unidad+")");
	int circularidadindex = mfrt.getFreeColumn("Circularidad");
	int formaindex = mfrt.getFreeColumn("Forma");
	int calidadindex = mfrt.getFreeColumn("Calidad");
	int relatadindex = mfrt.getFreeColumn("relación tamaño");
	int cali2index = mfrt.getFreeColumn("Calidad2");
	int volumedindex = mfrt.getFreeColumn("Volume ("+unidadv+")"); 
	int aspectoindex = mfrt.getFreeColumn("Aspecto");        
	int rojoindex = mfrt.getFreeColumn(et1);    
	int verdeindex = mfrt.getFreeColumn(et2);    
	int azulindex = mfrt.getFreeColumn(et3);    
	int rgbindex = mfrt.getFreeColumn(et4);   
	int disindex = mfrt.getFreeColumn("Dispersión"); 
	int colorindex = mfrt.getFreeColumn("Color"); 
	//mfrt.show("Resultados Conteo");
	art = 0;
	//mart = 0;
	lar = 0;
	anc = 0;
	per = 0;
	queb = 0;
	cir = 0;
	Tsu = 0;
	artgr = 0;
	vol = 0;
	nge = 0;
	graq=0; 
	grae= 0;
	for (int i = 0; i<n; i++) {
	// calcular promedios y variables//
	//double val = area[i];
	mart=calculateMean(area);
	art +=area[i]/n;		//art =art + area[i]/n;
	lar +=largo[i]/n;		//lar = lar + largo[i]/n;
	anc += ancho[i]/n;		//anc = anc + ancho[i]/n;
	per += perimetro[i]/n;	//per = per + perimetro[i]/n;
	cir += circularidad[i]/n;	//cir = cir + circularidad[i]/n;
	asp += aspecto[i]/n;
	rojos += rojo[i]/n;
	verdes += verde[i]/n;
	azules += azul[i]/n;
	prom = (rojo[i]+verde[i]+azul[i])/3;
	pro += prom/n;
	//queb = (area[i]/mart);
	queb = (area[i]/art);
	if (unidad=="mm") {
	if (area[i]>14.1) {arge += area[i]; nge= nge +1; arget= arge/nge;}
	}
	if (unidad=="cm") {
	if (area[i]>1.41) {arge += area[i]; nge= nge +1; arget= arge/nge;}
	}
	quebra=area[i]/arget;
	double xe = 1 / 3; 
	Ts = Math.PI*Math.pow(Math.pow((area[i]*ancho[i]), (0.3333)),2);
	Tsu = Tsu + Ts/n;
	artg = Math.PI*Math.pow((largo[i]*Math.pow(ancho[i],2)),(0.6666));
	artgr = artgr + artg/n;
	volume = Math.PI*largo[i]*Math.pow(ancho[i],2)/6;
	vol = vol + volume/n;
	dispersion = 0.5*perimetro[i]/Math.sqrt(Math.PI*area[i]);
	mfrt.incrementCounter();
	//mfrt.addLabel(lbl);
	//mfrt.addValue("lolo", "lolo");
	String pepe = "Resolución";
	//mfrt.addValue(etiquetaindex, 1);
	//mfrt.addValue("Número", col);
     	mfrt.setValue(etiquetaindex, i, "");
	mfrt.addValue(countindex, i+1); 
	mfrt.addValue(tamañoindex, area[i]);
	mfrt.addValue(tsindex, Ts);
	mfrt.addValue(artgindex, artg);
	mfrt.addValue(largoindex, largo[i]);
	mfrt.addValue(anchoindex, ancho[i]);
	mfrt.addValue(perimetroindex, perimetro[i]);
	mfrt.addValue(circularidadindex, circularidad[i]);
	mfrt.addValue(volumedindex, volume);
	if (circularidad[i]<=1.0)
	mfrt.setValue(formaindex, i, "Redondo");
	if (circularidad[i]<=0.756)
	mfrt.setValue(formaindex, i, "Ovoide");
	if (circularidad[i]<=0.5)
	mfrt.setValue(formaindex, i, "Alargado");
	if (unidad=="mm") {
	if (area[i]>14.1) 
	mfrt.setValue(calidadindex, i, "Grandes");
	if (area[i]<=14.1)
	mfrt.setValue(calidadindex, i, "Pequeños");
	if ((circularidad[i]<=0.65) && (area[i]>25) && (largo[i]>7.5))
	mfrt.setValue(calidadindex, i, "Unidos");
	if ((circularidad[i]<=0.6) && (area[i]>7) && (aspecto[i]>=2.8) && (ancho[i]>2.0) && (largo[i]>=9.0))
	mfrt.setValue(calidadindex, i, "Glumas");
	if ((circularidad[i]>=0.6) && (area[i]<=14.1) && (aspecto[i]<=1.9) && (largo[i]<=4.6))
	 {mfrt.setValue(calidadindex, i, "Quebrado"); graq=graq+1;} else {grae=grae+1;}
	}	
	if (unidad=="cm") {
	if (area[i]>1.41) 
	mfrt.setValue(calidadindex, i, "Grandes");
	if (area[i]<=1.41)
	mfrt.setValue(calidadindex, i, "Pequeños");
	if ((circularidad[i]<=0.65) && (area[i]>2.5) && (largo[i]>.75))
	mfrt.setValue(calidadindex, i, "Unidos");
	if ((circularidad[i]<=0.6) && (area[i]>0.7) && (aspecto[i]>=2.8) && (ancho[i]>0.2) && (largo[i]>=0.9))
	mfrt.setValue(calidadindex, i, "Glumas");
	if ((circularidad[i]>=0.6) && (area[i]<=1.41) && (aspecto[i]<=1.9) && (largo[i]<=0.46))
	 {mfrt.setValue(calidadindex, i, "Quebrado"); graq=graq+1;} else {grae=grae+1;}
	}
	mfrt.addValue(relatadindex, quebra);
	if (quebra<=0.75)
	mfrt.setValue(cali2index, i, "1"); else mfrt.setValue(cali2index, i, "0");
	mfrt.addValue(aspectoindex, aspecto[i]);
	mfrt.addValue(rojoindex, rojo[i]);
	mfrt.addValue(verdeindex, verde[i]);
	mfrt.addValue(azulindex, azul[i]);
	mfrt.addValue(rgbindex, prom);
	mfrt.addValue(disindex, dispersion);
	if (etiqueta==" RGB") {
	if (rojo[i]>140)
	mfrt.setValue(colorindex, i, "Cafe claro");
	if (rojo[i]<=140)
	mfrt.setValue(colorindex, i, "Cafe");
	if (rojo[i]<=133)
	mfrt.setValue(colorindex, i, "Cafe obscuro");
	}
	if (etiqueta==" Lab") {
	if (rojo[i]>50)
	mfrt.setValue(colorindex, i, "Cafe claro Lab");
	if (rojo[i]<=50)
	mfrt.setValue(colorindex, i, "Cafe Lab");
	if (rojo[i]<=47)
	mfrt.setValue(colorindex, i, "Cafe obscuro Lab");
	}
	//mfrt.updateResults();
	}
	for (int qq = 0; qq<4; qq++) {
	Double res = (anchura / 8.5);
	Double ellapsedTime = (System.currentTimeMillis()-iniciop)/1000;
	mfrt.setValue(etiquetaindex, 0, "Resolución: " +IJ.d2s(res,0));
	mfrt.setValue(etiquetaindex, 1, "Tiempo: "+IJ.d2s(ellapsedTime));
	mfrt.setValue(etiquetaindex, 2, "Fecha: "+date);
	mfrt.setValue(etiquetaindex, 3, "Hora: "+hora); 
	mfrt.show("Resultados Conteo "+lbl+etiqueta);
	}
 	IJ.saveAs("Results", dir+"Resultados Conteo "+lbl+" "+ext+etiqueta+".csv");
	IJ.run("Close");
                 */
                //crear cuadro de resultados con jtable

                //NumberFormat porc = NumberFormat.getPercentInstance();
                //porc.setMinimumFractionDigits(2);
                DecimalFormat porc = new DecimalFormat("#.##%");
                modelo = new DefaultTableModel();
                //table = new JTable(modelo);
                table.setColumnSelectionAllowed(true);
                table.setRowSelectionAllowed(true);
                table.setEnabled(true);
                /*
	JTableHeader header = table.getTableHeader();
	header.setEnabled(true);
       	//table.setBackground(Color.LIGHT_GRAY);
        //table.setForeground(Color.black);
        //Font font = new Font("",1,12);
        //table.setFont(font);
        table.setRowHeight(20);
	//table.setAutoscrolls(true);
	//table.setDragEnabled(true);
	//table.setAutoCreateRowSorter(true);
	//table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	table.setColumnSelectionAllowed(true);
	table.setRowSelectionAllowed(true);
	table.setTableHeader(header);
	table.getTableHeader();
                 */
                IJ.showProgress(10, 20);

                /*
            modelo.addColumn("Muestra: " + lbl);
            //modelo.addColumn("<html><center>Muestra:<br>" + lbl + "</br></center>");
            modelo.addColumn("Número");
            //modelo.addColumn("<html><center>Tamaño<br>(" + unidadc + ")</br></center>");
            modelo.addColumn("Tamaño (" + unidadc + ")");
            //modelo.addColumn("<html><center>Tamaño Ts<br>(" + unidadc + ")</br></center>");
            modelo.addColumn("Tamaño Ts (" + unidadc + ")");
            //modelo.addColumn("<html><center>Tamaño Total<br>(" + unidadc + ")</br></center>");
            modelo.addColumn("Tamaño Total (" + unidadc + ")");
            //modelo.addColumn("<html><center>Largo<br>(" + unidadselect + ")</br></center>");
            modelo.addColumn("Largo (" + unidadselect + ")");
            //modelo.addColumn("<html><center>Ancho<br>(" + unidadselect + ")</br></center>");
            modelo.addColumn("Ancho (" + unidadselect + ")");
            //modelo.addColumn("<html><center>Perímetro<br>(" + unidadselect + ")</br></center>");
            modelo.addColumn("Perímetro (" + unidadselect + ")");
            modelo.addColumn("Circularidad");
            modelo.addColumn("Forma");
            modelo.addColumn("Calidad");
            modelo.addColumn("Relación tamaño");
            modelo.addColumn("Calidad2");
            //modelo.addColumn("<html><center>Volumen<br>(" + unidadv + ")</br></center>");
            modelo.addColumn("Volume (" + unidadv + ")");
            modelo.addColumn("Aspecto");
            modelo.addColumn(et1);
            modelo.addColumn(et2);
            modelo.addColumn(et3);
            modelo.addColumn(et4);
            modelo.addColumn("Dispersión");
            modelo.addColumn("Color");
                 */
                String[] columnid = {"Muestra: " + lbl, "Número", "Tamaño (" + unidadc + ")", "Tamaño Ts (" + unidadc + ")", "Tamaño Total (" + unidadc + ")",
                    "Largo (" + unidadselect + ")", "Ancho (" + unidadselect + ")", "Perímetro (" + unidadselect + ")", "Circularidad",
                    "Forma", "Calidad", "Relación tamaño", "Calidad2", "Volume (" + unidadv + ")", "Aspecto", et1, et2, et3, et4, "Dispersión", "Color", "Brillo (%)"};
                modelo.setColumnIdentifiers(columnid);

                //table.setModel(modelo);
                IJ.showProgress(11, 20);
                Interfazmac.progressBar.setValue(70);
                /*
	String headers[] = new String[3];
    	headers[0] = "<html><center>Muestra:<br>Date</html>",
    	headers[1] = "Número";
    	headers[2] = "<html><center>Long<br>Centered</br></center></html>";
	modelo.setColumnIdentifiers(headers);
                 */
                double brillom = 0;
                art = 0;
                //mart = 0;
                lar = 0;
                anc = 0;
                per = 0;
                queb = 0;
                cir = 0;
                asp = 0;
                Tsu = 0;
                artgr = 0;
                vol = 0;
                nge = 0;
                graq = 0;
                int graqi = 0;
                grae = 0;
                int graei = 0;
                // se incrementan los valores en resumen si no se iguala a cero
                rojos = 0;
                verdes = 0;
                azules = 0;
                prom = 0;
                double tonos = 0.0;
                double saturas = 0.0;
                double brillos = 0.0;
                pro = 0;
                quebra = 0;
                double entec = 0.0;
                for (int i = 0; i < n; i++) {
                    brillom = (100 * (rojo[i] * 0.299 + verde[i] * 0.587 + azul[i] * 0.114)) / 255;
//System.out.println("Grano "+(i+1)+" Tono "+dec.format(tono[i])+" Saturación "+dec.format(satura[i])+" Brillo "+dec.format(brillo[i]) );
//System.out.println("Grano "+(i+1)+" Tono "+dec.format(rojo[i])+" Saturación "+dec.format(verde[i])+" Brillo "+dec.format(azul[i]) );
                    art += area[i] / n;		//art =art + area[i]/n;
                    lar += largo[i] / n;		//lar = lar + largo[i]/n;
                    anc += ancho[i] / n;		//anc = anc + ancho[i]/n;
                    per += perimetro[i] / n;	//per = per + perimetro[i]/n;
                    cir += circularidad[i] / n;	//cir = cir + circularidad[i]/n;
                    asp += aspecto[i] / n;
                    rojos += rojo[i] / n;
                    verdes += verde[i] / n;
                    azules += azul[i] / n;
                    prom = (rojo[i] + verde[i] + azul[i]) / 3;
                    pro += prom / n;
                    //queb = (area[i]/mart);
                    queb = (area[i] / mart);

                    double npart = (unidadselect.equals("cm")) ? 0.141 : 14.1;

                    if (area[i] > npart) {
                        arge += area[i];
                        nge = nge + 1;
                        arget += arge / nge;
                    }

                    //System.err.println("nombre arget " + arget);
                    /*
	if (unidad=="mm") {
	if (area[i]>14.1) {arge += area[i]; nge= nge +1; arget= arge/nge;}
	}
	if (unidad=="cm") {
	if (area[i]>1.41) {arge += area[i]; nge= nge +1; arget= arge/nge;}
	}
                     */
                    quebra = area[i] / arget; //revisar hay error
                    double xe = 1 / 3;
                    Ts = Math.PI * Math.pow(Math.pow((area[i] * ancho[i]), (0.3333)), 2);
                    Tsu = Tsu + Ts / n;
                    artg = Math.PI * Math.pow((largo[i] * Math.pow(ancho[i], 2)), (0.6666));
                    artgr = artgr + artg / n;
                    volume = Math.PI * largo[i] * Math.pow(ancho[i], 2) / 6;
                    vol = vol + volume / n;
                    dispersion = 0.5 * perimetro[i] / Math.sqrt(Math.PI * area[i]);

                    String tamaño = area[i] > 14.1 ? "Grandes" : area[i] <= 14.1 ? "Pequeños" : (circularidad[i] <= 0.65) && (area[i] > 25) && (largo[i] > 7.5) ? "Unidos" : (circularidad[i] <= 0.6) && (area[i] > 7) && (aspecto[i] >= 2.8) && (ancho[i] > 2.0) && (largo[i] >= 9.0) ? "Glumas" : (circularidad[i] >= 0.6) && (area[i] <= 14.1) && (aspecto[i] <= 1.9) && (largo[i] <= 4.6) ? "Quebrados" : "";
                    if (unidadselect.equals("cm")) {
                        tamaño = area[i] > 0.141 ? "Grandes" : area[i] <= 0.141 ? "Pequeños" : (circularidad[i] <= 0.65) && (area[i] > 0.025) && (largo[i] > .75) ? "Unidos" : (circularidad[i] <= 0.6) && (area[i] > 0.07) && (aspecto[i] >= 2.8) && (ancho[i] > 0.2) && (largo[i] >= 0.9) ? "Glumas" : (circularidad[i] >= 0.6) && (area[i] <= 0.141) && (aspecto[i] <= 1.9) && (largo[i] <= 0.46) ? "Quebrados" : "";
                    }
                    String forma = ((circularidad[i] <= 0.5) ? "Alargado" : (circularidad[i] <= 0.756) ? "Ovoide" : "Redondo");
                    String color = ((rojo[i] <= 133) ? "Cafe obscuro" : (rojo[i] <= 140) ? "Cafe" : "Cafe claro");
                    if (etiqueta.equals("Lab")) {
                        color = ((rojo[i] <= 47) ? "Cafe obscuro Lab" : (rojo[i] <= 50) ? "Cafe Lab" : "Cafe claro Lab");
                    }
                    entec += ((area[i] / mart) <= 0.75) ? 1.0 : 0.0;
                    String grape = ((area[i] / mart) <= 0.75) ? "Pequeño" : circularidad[i] / mcir >= 0.75 ? "Normal" : "impureza";

                    modelo.addRow(new Object[]{"", 1 + i, dec.format(area[i]), dec.format(Ts), dec.format(artg), dec.format(largo[i]), dec.format(ancho[i]), dec.format(perimetro[i]),
                        dec.format(circularidad[i]), forma, tamaño, dec.format(area[i] / mart), grape, dec.format(volume), dec.format(aspecto[i]), dec.format(rojo[i]), dec.format(verde[i]), dec.format(azul[i]), dec.format(prom), dec.format(dispersion), color, brillom});
                    table.setModel(modelo); //se movio para acá

                    double nlarg = (unidadselect.equals("cm")) ? 0.46 : 4.6;
                    if ((circularidad[i] >= 0.6) && (area[i] <= npart) && (aspecto[i] <= 1.9) && (largo[i] <= nlarg)) {
                        //table.setValueAt("Quebrado", i, 10);
                        graq = graq + 1;
                    }
                }
                //brillom = (100*(rojo[i] * 0.299 + verde[i] * 0.587 + azul[i] * 0.114))/255;
                double brightness = (100 * (rojos * 0.299 + verdes * 0.587 + azules * 0.114)) / 255;
                //System.out.println("Grano "+(i+1)+" brightness "+dec.format(brightness) );
                grae = n - graq;

                //System.err.println("grano pequeño " + total + " grano entero "+ grae);
                //modelo.setValueAt(dec.format(total), i ,3); 
                //IJ.log(""+(total/n));
                IJ.showProgress(12, 20);
                Interfazmac.progressBar.setValue(75);
                modelo = (DefaultTableModel) table.getModel();
                modelo.fireTableDataChanged();

                if (n <= 3) {
                    for (int id = n; id < 4; id++) {
                        modelo.addRow(new Object[]{""});
                        for (int j = 1; j < modelo.getColumnCount(); j++) {
                            Object obs = modelo.getValueAt(id, j);
                            if (obs == null || obs.toString().isEmpty()) {
                                modelo.setValueAt("", id, j);
                            }
                        }
                    }
                }
                resol = (int) (((anchura / 8.5) + 99) / 100) * 100;
                table.setValueAt("Resolución: " + resol, 0, 0);
                Double ellapsedTimes = (System.currentTimeMillis() - iniciop) / 1000;
                table.setValueAt("Tiempo: " + time(), 1, 0);
                table.setValueAt("Fecha: " + date, 2, 0);
                table.setValueAt("Hora: " + hora, 3, 0);
                for (int i = 4; i < n; i++) {
                    table.setValueAt("", i, 0);
                }

                //table.moveColumn(table.getColumnCount() -1, 0);
                //move(table, 0, 9);
                //table.data.change();
/*
 	JButton aceptar = new JButton("Guardar");
	//aceptar.setPreferredSize(new Dimension(12, 12)); 
	//aceptar.addActionListener(this);
	aceptar.addActionListener(new ActionListener()
	{public void actionPerformed(ActionEvent evt)
	{ guardaResultados();}
	});
                 */
                final String cNames[] = new String[table.getColumnCount()];
                final Object cols[] = new Object[table.getColumnCount()];
                for (int i = 0; i < cNames.length; i++) {
                    cNames[i] = table.getColumnName(i);
                    cols[i] = table.getColumnModel().getColumn(i);
                }
                table.setFillsViewportHeight(true);
                table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                //table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF );  
                //table.setSize(new Dimension(750,660));								
                screpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                screpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                //screpane.setSize(800, 800);
                //table.setPreferredScrollableViewportSize(new Dimension(750, 660)); //llena el panel con la tabla
                screpane.setViewportView(table);
                //ImageJ imageJ = ij.IJ.getInstance();
                //JFrame myJFrame = new JFrame(); 
                //table.setComponentPopupMenu(Interfazmac.popupmenu);
                myJFrame.setIconImage((icon).getImage());
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                myJFrame.setBounds(0, 0, screenSize.width, screenSize.height - 40);
                myJFrame.setSize(780, 705);
                //myJFrame.add(new JScrollPane(table));
                //myJFrame.setJMenuBar(Interfazmac.tablemenu);
                myJFrame.add(screpane);
                //myJFrame.add(aceptar, BorderLayout.SOUTH);;
                myJFrame.setTitle("Resultados Conteo " + lbl + " " + ext + etiqueta);
                //myJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                if (Interfazmac.verres.isSelected()) {
                    myJFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                } else {
                    myJFrame.setExtendedState(JFrame.ICONIFIED);
                }
                myJFrame.setLocationRelativeTo(null);
                myJFrame.pack();
                myJFrame.toFront();
                //myJFrame.setAlwaysOnTop(Interfazmac.verres.isSelected()); 
                myJFrame.setVisible(Interfazmac.verres.isSelected());
                IJ.showProgress(14, 20);
                Interfazmac.progressBar.setValue(80);
                /*
	JTable table = new JTable();
	JScrollPane spTable = new JScrollPane(table);
	JPanel panel = new JPanel();
	panel.add(spTable);

                 */

 /*
	Frame frame = WindowManager.getFrame("Resumen Conteo.csv");
	if (frame!=null && (frame instanceof TextWindow)) {
	TextWindow tw = (TextWindow)frame;
	ResultsTable table = tw.getTextPanel().getResultsTable();
	if (table!= null)
	ResumenConteo = table;}
	if (ResumenConteo==null)	
	ResumenConteo = new ResultsTable();
	ResumenConteo.incrementCounter();
	ResumenConteo.addValue("Muestra", lbl);
	ResumenConteo.addValue("Extensión", ext);
	Double res = (anchura / 8.5);
	ResumenConteo.addValue("Resolución", IJ.d2s(res,0));
	ResumenConteo.addValue("Número de granos", n);
	ResumenConteo.addValue("Granos quebrados", graq);
	ResumenConteo.addValue("Tamaño ("+unidadc+")", art); 
	ResumenConteo.addValue("Tamaño Ts ("+unidadc+")", Tsu);
	ResumenConteo.addValue("Tamaño Total ("+unidadc+")", artgr);
	ResumenConteo.addValue("Largo ("+unidad+")", lar);
	ResumenConteo.addValue("Ancho ("+unidad+")", anc);
	ResumenConteo.addValue("Perímetro ("+unidad+")", per);
	ResumenConteo.addValue("Circularidad", cir);
	ResumenConteo.addValue("Volumen ("+unidadv+")", vol);
	ResumenConteo.addValue("Aspecto", asp);
	if (cir<=1.0)
	ResumenConteo.addValue("Forma", "Redondo");
	if (cir<=0.756) 
	ResumenConteo.addValue("Forma", "Ovoide");
	if (cir<=0.5)
	ResumenConteo.addValue("Forma", "Alargado");
	ResumenConteo.addValue(et1, rojos);
	ResumenConteo.addValue(et2, verdes);
	ResumenConteo.addValue(et3, azules);
	ResumenConteo.addValue(et4, pro);
	if (etiqueta==" RGB") {
	if (rojos>140)
	ResumenConteo.addValue("Color", "Cafe claro");
	if (rojos<=140)
	ResumenConteo.addValue("Color", "Cafe");
	if (rojos<=133)
	ResumenConteo.addValue("Color", "Cafe obscuro");
	}
	if (etiqueta==" Lab") {
	if (rojos>50)
	ResumenConteo.addValue("Color", "Cafe claro Lab");
	if (rojos<=50)
	ResumenConteo.addValue("Color", "Cafe Lab");
	if (rojos<=47)
	ResumenConteo.addValue("Color", "Cafe obscuro Lab");
	}
	Double finpro = (System.currentTimeMillis()-iniciop)/1000;
	ResumenConteo.addValue("Tiempo", IJ.d2s(finpro));
	finproc = (System.currentTimeMillis()-startTime)/1000;
	//ResumenConteo.addValue("Tiempos", IJ.d2s(startTime));
	//ResumenConteo.updateResults();
	//ResumenConteo.addResults();
	ResumenConteo.show("Resumen Conteo.csv");
	IJ.saveAs("Results", dir+"Resumen Conteo.csv");
 	IJ.selectWindow("Resumen Conteo.csv");
                 */
                //crear resumen con jtable
                //DefaultTableModel resumen = new DefaultTableModel();
                //resumen = tableres.getModel();
                String[] columnresu = {"Muestra", "Extensión", "Resolución", "Número de granos", "Granos quebrados", "Granos pequeños (%)", "Tamaño (" + unidadc + ")",
                    "Tamaño Ts (" + unidadc + ")", "Tamaño Total (" + unidadc + ")", "Largo (" + unidadselect + ")", "Ancho (" + unidadselect + ")", "Perímetro (" + unidadselect + ")", "Circularidad",
                    "Volume (" + unidadv + ")", "Aspecto", "Forma", et1, et2, et3, et4, "Color", "Brillo", "Tiempo"};
                resumen.setColumnIdentifiers(columnresu);

                String[] column = {"Muestra", "Extensión", "<html><center>Resolución<br>(ppp)</br></center>", "<html><center>Número<br> de granos </br></center>",
                    "<html><center>Granos<br> quebrados </br></center>", "<html><center>Granos<br>pequeños (%)</br></center>", "<html><center>Tamaño<br>(" + unidadselect + "²)</br></center>",
                    "<html><center>Tamaño<br> Ts (" + unidadselect + "²)</br></center>", "<html><center>Tamaño<br> Total (" + unidadselect + "²)</br></center>",
                    "<html><center>Largo<br>(" + unidadselect + ")</br></center>", "<html><center>Ancho<br>(" + unidadselect + ")</br></center>", "<html><center>Perímetro<br>(" + unidadselect + ")</br></center>",
                    "<html><center>Volumen<br>(" + unidadselect + "³)</br></center>", "Circularidad", "Aspecto", "Forma",
                    et1, et2, et3, et4, "Color", "Brillo (%)", "<html><center>Tiempo<br>(s)</br></center>"};
                Interfazmac.resumen2.setColumnIdentifiers(column);
                //Object[][] data={ {lbl, ext , resol, n , graq , art , Tsu, artgr, lar, anc, per, cir, vol, asp, "", rojos, verdes, azules, pro, finpro}};
                //Object[][] data={ {null, null , null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}};
                //Object[][] data = null;
                //resumen.setColumnCount(0);
                //resumen.setRowCount(0);

                IJ.showProgress(15, 20);
                Interfazmac.progressBar.setValue(85);
                tableres = new JTable();
                //Color fondopanel = Color.decode("#C89600");
                //JTable tableres = new JTable(); 
                tableres.getTableHeader().setOpaque(false);
                tableres.getTableHeader().setBackground(fondopanel);
                JTableHeader th = tableres.getTableHeader();
                TableColumnModel tcm = th.getColumnModel();
                //tableres.setTransferHandler(new LabelTransferHandler());
                KeyStroke copy = KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK, false);
                // Identifying the copy KeyStroke user can modify this
                // to copy on some other Key combination.
                KeyStroke paste = KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK, false);
                // Identifying the Paste KeyStroke user can modify this
                //to copy on some other Key combination.
                tableres.registerKeyboardAction(null, "Copy", copy, JComponent.WHEN_FOCUSED);
                tableres.registerKeyboardAction(null, "Paste", paste, JComponent.WHEN_FOCUSED);
                system = Toolkit.getDefaultToolkit().getSystemClipboard();
                tableres.addMouseListener(null);
                tableres.getTableHeader().addMouseListener(null);
                Double finpro = (System.currentTimeMillis() - iniciop) / 1000;

                String format = null;
                if (cir <= 1.0) {
                    format = "Redondo";

                }
                if (cir <= 0.756) {

                    format = "Ovoide";

                }
                if (cir <= 0.5) {
                    format = "Alargado";

                }

                String formas = ((cir <= 0.5) ? "Alargado" : (cir <= 0.756) ? "Ovoide" : "Redondo");
                String colors = ((rojos <= 133) ? "Cafe obscuro" : (rojos <= 140) ? "Cafe" : "Cafe claro");
                if (etiqueta.equals("Lab")) {
                    colors = ((rojos <= 47) ? "Cafe obscuro Lab" : (rojos <= 50) ? "Cafe Lab" : "Cafe claro Lab");
                }
                Object[] row = {lbl, ext, resol, n, graq, dec.format(entec / n * 100), dec.format(art), dec.format(Tsu), dec.format(artgr),
                    dec.format(lar), dec.format(anc), dec.format(per), dec.format(vol), dec.format(cir), dec.format(asp), formas,
                    dec.format(rojos), dec.format(verdes), dec.format(azules), dec.format(pro), colors, dec.format(brightness), dec.format(finpro)};
                resumen.addRow(row);
                Interfazmac.resumen2.addRow(row);
                for (int columnas = 0; columnas < Interfazmac.tresu.getColumnCount(); columnas++) {
                    DefaultTableCellRenderer rendar = new DefaultTableCellRenderer();
                    rendar.setHorizontalAlignment(JLabel.CENTER);
                    Interfazmac.tresu.getColumnModel().getColumn(columnas).setCellRenderer(rendar);
                }
                Interfazmac.tresu.setComponentPopupMenu(Interfazmac.popupmenu);
                table.setComponentPopupMenu(Interfazmac.popupmenu);
                tableres.setComponentPopupMenu(Interfazmac.popupmenu);
                boolean savesi = (Interfazmac.tresu.getRowCount() <= 0) ? false : true;
                //Interfazmac.configana.setEnabled(savesi);
                Interfazmac.guardartab.setEnabled(savesi);
                Interfazmac.guardartabc.setEnabled(savesi);
                //Interfazmac.guartabxls.setEnabled(savesi);
                tableres.setModel(resumen);
                //Interfazmac.tresu.setModel(resumen);
                tableres.setAutoCreateColumnsFromModel(false);
                nc = tableres.getRowCount();
                //nc=Interfazmac.resumen2.getRowCount();

                tableres.setFillsViewportHeight(true);
                tableres.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                //tableres.setAutoResizeMode(JTable.AUTO_RESIZE_OFF ); 
                TableColumnAdjuster tca = new TableColumnAdjuster(tableres);
                tca.adjustColumns();
                //tableres.setSize(new Dimension(1570,780));  
                //tableres.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
                //resumen.fireTableDataChanged();
                JViewport jv = new JViewport();
                jv.setView(tableres);
                //scrollPane = new JScrollPane(tableres); 
                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                //tableres.setPreferredScrollableViewportSize(new Dimension(1570, 780)); //llena el panel con la tabla
                JPanel tablePanel = new JPanel(new BorderLayout());
                tablePanel.setBackground(fondopanel);
                tablePanel.add(scrollPane, BorderLayout.CENTER);
                //JScrollPane scrollPane = new JScrollPane(tableres);
                scrollPane.setViewportView(tableres);
                //JScrollPane scrollPane = new JScrollPane(tableres);
                //ImageJ imageJ = ij.IJ.getInstance();
                //JFrame Fresumen = new JFrame(); 
                //ImageIcon icon = new ImageIcon(imageJ.getClass().getResource("/MACGRAIN-IJ.gif"));
                JLabel lblHeading = new JLabel("Resumen de mediciones");
                //lblHeading.setFont(new Font("Arial",Font.TRUETYPE_FONT,24));
                Fresumen.getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, fondopanel));
                Fresumen.getContentPane().add(lblHeading, BorderLayout.PAGE_START);
                Fresumen.setIconImage((icon).getImage());
                //Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                Fresumen.setBounds(0, 0, screenSize.width, screenSize.height - 40);
                Fresumen.setSize(1570, 780);
                Fresumen.addWindowListener(null);
                Fresumen.add(scrollPane);
                //Fresumen.add(aceptar, BorderLayout.SOUTH);;
                //Fresumen.setTitle("Resumen Conteo");
                //Fresumen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
                Fresumen.setExtendedState(JFrame.ICONIFIED);
                Fresumen.setLocationRelativeTo(null);
                Fresumen.setResizable(true);
                Fresumen.pack();
                //Fresumen.setAlwaysOnTop(true);
                Fresumen.toFront();
                Fresumen.setVisible(Interfazmac.verresu.isSelected()); // se envió a la pestaña de la interfaz
                //guardaResultados();
                GuardarSalida fileSaver = new GuardarSalida(lbl, ext, etiqueta, table);
                fileSaver.guardaResultados();
//fileSaver.guardaResumen();
                //guardaResultado();
                /*
            if (Interfazmac.guatab.isSelected()) {
                guardaResultados();
                guardaResultado();
            }
                 */
                IJ.showProgress(17, 20);
                Interfazmac.progressBar.setValue(90);
                /*
	int lastRow = tableres.convertRowIndexToView(resumen.getRowCount() - 1);
	tableres.setRowSelectionInterval(lastRow, lastRow);
	int j = 1;
	while(j <tableres.getRowCount()){
	j++;}
	//modelo.addRow( new Object[]{
	Object[] row = { lbl, ext , resol, n , graq , art , Tsu, artgr, lar, anc, per, cir, vol, asp, "", rojos, verdes, azules, pro, finpro, j};
	resumen.addRow(row);
	tableres.setValueAt(lbl, j, 0);
	tableres.setValueAt(ext, j, 1);
	Double resolu = (anchura / 8.5);
	tableres.setValueAt(resol, j, 2);
	tableres.setValueAt(n, j, 3);
	tableres.setValueAt(graq, i, 4);
	tableres.setValueAt(art, j, 5); 
	tableres.setValueAt(Tsu, j, 6);
	tableres.setValueAt(artgr, j, 7);
	tableres.setValueAt(lar, j, 8);
	tableres.setValueAt(anc, j, 9);
	tableres.setValueAt(per, j, 10);
	tableres.setValueAt(cir, j, 11);
	tableres.setValueAt(vol, j, 12);
	tableres.setValueAt(asp, j, 13);
	if (cir<=1.0)
	tableres.setValueAt("Redondo", j, 14);
	if (cir<=0.756)
	tableres.setValueAt("Ovoide", j, 14);
	if (cir<=0.5)
	tableres.setValueAt("Alargado", j, 14);
	tableres.setValueAt(rojos, j, 15);
	tableres.setValueAt(verdes, j, 16);
	tableres.setValueAt(azules, j, 17);
	tableres.setValueAt(pro, j, 18);
	tableres.setValueAt(finpro, j, 19);
 	JButton aceptar = new JButton("Guardar");
	//aceptar.setPreferredSize(new Dimension(12, 12)); 
	//aceptar.addActionListener(this);
	aceptar.addActionListener(new ActionListener()
	{public void actionPerformed(ActionEvent evt)
	{ guardaResultados();}
	});
                 */
                //guardaResultados();
                dec = new DecimalFormat("0.000");
                //time = (System.currentTimeMillis() - iniciop) / 1000.0D;
                timef = timef + time();
                if (nimage >= totimage) {
                    //Interfazmac.statusBar.setText("El análisis se realizó en " + nimage + " imágenes, en un tiempo total de: " + dec.format(timef) + " s, con un promedio por imagen de " + dec.format(timef / nimage) + " s");
                    IJ.showStatus("El análisis se realizó en " + nimage + " imágenes, en un tiempo total de: " + dec.format(timef) + " s, con un promedio por imagen de " + dec.format(timef / nimage) + " s");
                    //IJ.log("Cantidad de imagenes analizadas ("+nimage+"), en un tiempo total de: "+dec.format(timef)+" s, con un promedio por imagen de "+dec.format(timef/nimage)+" s");	
                    //IJ.showStatus("Imagenes analizadas: " +startTime);
                    timefin = System.currentTimeMillis();

                    if (Interfazmac.guatab.isSelected()) {
                        //guardaResumen();
                        GuardarSalida fileSaveres = new GuardarSalida(lbl, ext, etiqueta, tableres);
                        fileSaveres.guardaResumen();
                    }
                }

                if (Frlog instanceof TextWindow) {
                    ((TextWindow) Frlog).setTitle("Registro de eventos");
                    //((TextWindow) Frlog).setIconImage((icon).getImage());
                    Frlog.setMenuBar(Interfazmac.mblog);
                    Frlog.setIconImage((icon).getImage());
                    Frlog.setLocationRelativeTo(Interfazmac.myJFrame);
                    Frlog.setAlwaysOnTop(true);
                    Frlog.setVisible(true);

                }
                //final ResultsTable rt3 = ResultsTable.getResultsTable(); 
/* 
	if (WindowManager.getFrame("Resumen Conteo") != null) {
	 IJ.selectWindow("Resumen Conteo");
	ResumenConteo.updateResults();
	 IJ.saveAs("Results", dir+"Resumen Conteo.csv");
	 //IJ.saveAs("Results", dir+"Resumen Conteo.csv");
	}
                 */
                IJ.showProgress(18, 20);

                /*
            if (Interfazmac.vpis.isSelected()) {

                imp2.show();
                imp2.setTitle("Imagen " + lbl + " segmentada con umbral " + umbralseg + " "+ labelumbral);
                Frame Fco = WindowManager.getFrame(imp2.getTitle());
                if (Fco instanceof ImageWindow) {
                    Fco.setIconImage(((icon).getImage()));
                }
                //Fco.setTitle("Imagen "+lbl+" segmentada");
                Fco.setLocationRelativeTo(Interfazmac.myJFrame);
                //Fco.setLocation((vpos-636)/2, (hpos-882)/2);
                Fco.setSize(636, 882);
                Toolbar.getInstance().setTool(Toolbar.HAND);
                //IJ.setTool("hand");
            } else {
                rm.close();
                closeAll();
            }
                 */
                // thread.run();
                //thread.sleep(1);
                Interfazmac.progressBar.setValue(100);
                Interfazmac.progressBar.setString("El procesamiento finalizó");
            }
            // Cuando el procesamiento esté completo, reinicia la barra de progreso

        }
        );

        processingThread.start();
    }

    JFrame variable;

    public void agregarJFrame(JFrame Fresumen) {
        variable = Fresumen;
    }

    public double time() {

        return time = (System.currentTimeMillis() - iniciop) / 1000.0D;
        //return ends - iniciop;
    }

    /*
	static class DecimalFormatRenderer extends DefaultTableCellRenderer {
	private static final DecimalFormat formatter = new DecimalFormat( "#.00" );
	public Component getTableCellRendererComponent(
	JTable table, Object value, boolean isSelected,
	boolean hasFocus, int row, int column) {
	// First format the cell value as required
	value = formatter.format((Number)value);
	// And pass it on to parent class
	return super.getTableCellRendererComponent(
	table, value, isSelected, hasFocus, row, column );
	}
	}
     */
 /*
	private void guardaResultados(){
        try {
	FileWriter excel = new FileWriter(dir+"Resultados Conteo "+lbl+" "+ext+etiqueta+".csv");
	 //BufferedWriter excel = new BufferedWriter(new FileWriter(dir+"Resultados Conteo "+lbl+" "+ext+etiqueta+".csv"));
	for(int i = 0; i < modelo.getColumnCount(); i++){
	excel.write(modelo.getColumnName(i) + "\t");
	}
	excel.write("\n");
	for(int i=0; i< modelo.getRowCount(); i++) {
	for(int j=0; j < modelo.getColumnCount(); j++) {
	excel.write(modelo.getValueAt(i,j).toString()+"\t");
	}
	excel.write("\n");
	}
	excel.close();
	}catch(IOException e){ System.out.println(e); }
	}
     */
 /*
	private void guardaResultados(){
	try {
        //String sucursalesCSVFile = "src/archivos/DatosTabla.txt";
	//FileWriter csv = new FileWriter(new File(dir+"Resultados Conteo "+lbl+" "+ext+etiqueta+".csv"));
	BufferedWriter csv = new BufferedWriter(new FileWriter(dir+"Resultados Conteo "+lbl+" "+ext+etiqueta+".csv"));
	for(int i = 0; i < modelo.getColumnCount(); i++){
	csv.write(modelo.getColumnName(i) + "\t");
	}
	csv.write(",");
	for(int i=0; i< modelo.getRowCount(); i++) {
	for(int j=0; j < modelo.getColumnCount(); j++) {
	csv.write(modelo.getValueAt(i,j).toString()+"\t");
	}
	csv.write(",");
	}
	csv.close();
	}catch(IOException e){ System.out.println(e); }
	}
     */
    @Override
    public void windowClosing(WindowEvent e) {
        for (int i = resumen.getRowCount(); i > 0; --i) {
            resumen.removeRow(i - 1);
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }
    //popupmenu

    public void mouseClicked(MouseEvent e) {
        /*
        if (e.getButton() == MouseEvent.BUTTON3) {
            popupmenu.show(e.getComponent(), e.getX(), e.getY());
        }
         */
        Object o = e.getSource();
        if (o instanceof JMenuItem) {
            JMenuItem men = (JMenuItem) o;
            if (men.getText().equals("Copiar columna")) {
                int col = tableres.getTableHeader().columnAtPoint(e.getPoint());
                StringSelection selection = new StringSelection(tableres.getColumnName(col));
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, selection);
            }
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    //Esto es para el boton iniciar y reiniciar
    public void actionPerformed(ActionEvent evt) {
        String sep = System.getProperty("file.separator");
        Object accion = evt.getSource();
        if (evt.getActionCommand() == "ABRIRICA") {
            JMenuItem menuItem = (JMenuItem) evt.getSource();
            //IJ.open(menuItem.getText());
            Opener op = new Opener();
            op.setSilentMode(true);
            File arca = new File(menuItem.getText());
            if (!arca.exists()) {
                //IJ.log("La ruta no existe");
                String msg = "No se encontró el archivo o la ruta no existe: " + menuItem.getText();
                JOptionPane panee = new JOptionPane(getMessage(msg, 400), JOptionPane.ERROR_MESSAGE, 0, icon2, new String[]{"Aceptar"}, "Aceptar");
                JDialog dialoge = panee.createDialog(Interfazmac.myJFrame, "Error al abrir el archivo");
                dialoge.setIconImage((icon).getImage());
                dialoge.setAlwaysOnTop(true);
                dialoge.toFront();
                dialoge.setVisible(true);
                /*
	JOptionPane.showMessageDialog(null, "No se encontró el archivo o la ruta no existe",
	"Error al abrir el archivo",
	JOptionPane.ERROR_MESSAGE);
	return;
                 */
            } else {
                op.open(menuItem.getText());
            }
        }
        Object o = evt.getSource();
        if (o instanceof JMenuItem) {
            JMenuItem men = (JMenuItem) o;
            if (men.equals(guardimc) || men.equals(guardim)) {
                //img = imp.getImage();
                imp = IJ.getImage();
                showMessageBox(filename, dirname, ancho, path, tfaltante);

            }
            if (men.getText().equals("Copiar")) {
                // StringBuilder sbf = new StringBuilder();
                StringBuilder sbf = new StringBuilder();

                // Check to ensure we have selected only a contiguous block of cells.
                final int numcols = tableres.getSelectedColumnCount();
                final int numrows = tableres.getSelectedRowCount();
                final int[] rowsselected = tableres.getSelectedRows();
                final int[] colsselected = tableres.getSelectedColumns();

                if (!((numrows - 1 == rowsselected[rowsselected.length - 1] - rowsselected[0]
                        && numrows == rowsselected.length)
                        && (numcols - 1 == colsselected[colsselected.length - 1] - colsselected[0]
                        && numcols == colsselected.length))) {
                    JOptionPane.showMessageDialog(null, "Invalid Copy Selection",
                            "Invalid Copy Selection",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                for (int i = 0; i < numrows; i++) {
                    for (int j = 0; j < numcols; j++) {
                        sbf.append(tableres.getValueAt(rowsselected[i], colsselected[j]));
                        if (j < numcols - 1) {
                            sbf.append('\t');
                        }
                    }
                    sbf.append('\n');
                }
                stsel = new StringSelection(sbf.toString());
                clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stsel, stsel);

            }
            if (men.getText().equals("Pegar")) {
                final int startRow = (tableres.getSelectedRows())[0];
                final int startCol = (tableres.getSelectedColumns())[0];
                try {
                    final String trString = (String) (clipboard.getContents(this).getTransferData(DataFlavor.stringFlavor));
                    System.out.println("String is:" + trString);
                    final StringTokenizer st1 = new StringTokenizer(trString, "\n");
                    for (int i = 0; st1.hasMoreTokens(); i++) {
                        rowstring = st1.nextToken();
                        StringTokenizer st2 = new StringTokenizer(rowstring, "\t");
                        for (int j = 0; st2.hasMoreTokens(); j++) {
                            value = (String) st2.nextToken();
                            if (startRow + i < tableres.getRowCount()
                                    && startCol + j < tableres.getColumnCount()) {
                                tableres.setValueAt(value, startRow + i, startCol + j);
                            }
                            System.out.println("Putting " + value + "at row = " + startRow + i + " column = " + startCol + j);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (men.getText().equals("Copiar todo")) {
                // StringBuilder sbf = new StringBuilder();
                StringBuilder sbf = new StringBuilder();
                StringBuilder sbfcn = new StringBuilder();
                // Check to ensure we have selected only a contiguous block of cells.
                final int numcols = tableres.getColumnCount();
                final int numrows = tableres.getRowCount();
                final int[] rowsselected = tableres.getSelectedRows();
                final int[] colsselected = tableres.getSelectedColumns();

                /*
      	final String cNames[] = new String[tableres.getColumnCount()];
        final Object cols[] = new Object[tableres.getColumnCount()];
        for (int i = 0; i < cNames.length; i++) {
        cNames[i] = tableres.getColumnName(i);
        cols[i] = tableres.getColumnModel().getColumn(i);
	sbf.append(tableres.getColumnName(i));
	sbf.append('\t');
        }
        for (int index = 0; index < resumen.getColumnCount(); index++) {
        sbf.append('\t').append(resumen.getColumnName(index)).append('\t');
        }
                 */
                for (int i = 0; i < 1; i++) {
                    for (int j = 0; j < numcols; j++) {
                        sbf.append(tableres.getColumnName(j));
                        if (j < numcols - 1) {
                            sbf.append('\t');
                        }
                    }
                    sbf.append('\n');
                }
                for (int i = 0; i < numrows; i++) {
                    for (int j = 0; j < numcols; j++) {
                        sbf.append(tableres.getValueAt(i, j));
                        if (j < numcols - 1) {
                            sbf.append('\t');
                        }
                    }
                    sbf.append('\n');
                }
                stsel = new StringSelection(sbf.toString());
                stselcn = new StringSelection(sbfcn.toString());
                clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                //clipboard.setContents(stselcn, null);
                clipboard.setContents(stsel, stsel);
            }
            if (men.getText().equals("Cortar")) {
                final int numcols = tableres.getSelectedColumnCount();
                final int numrows = tableres.getSelectedRowCount();
                final int[] rowsselected = tableres.getSelectedRows();
                final int[] colsselected = tableres.getSelectedColumns();
                /*
	int numCols=table.getSelectedColumnCount();
	int numRows=table.getSelectedRowCount();
	int[] rowsSelected=table.getSelectedRows();
	int[] colsSelected=table.getSelectedColumns();
                 */
                if (!((numrows - 1 == rowsselected[rowsselected.length - 1] - rowsselected[0]
                        && numrows == rowsselected.length)
                        && (numcols - 1 == colsselected[colsselected.length - 1] - colsselected[0]
                        && numcols == colsselected.length))) {
                    /*
	if (numRows!=rowsSelected[rowsSelected.length-1]-rowsSelected[0]+1 || numRows!=rowsSelected.length ||
	numCols!=colsSelected[colsSelected.length-1]-colsSelected[0]+1 || numCols!=colsSelected.length) {
                     */
                    JOptionPane.showMessageDialog(null, "Invalid Copy Selection", "Invalid Copy Selection", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                /*StringBuffer excelStr=new StringBuffer(); */
                StringBuilder sbf = new StringBuilder();
                for (int i = 0; i < numrows; i++) {
                    for (int j = 0; j < numcols; j++) {
                        sbf.append(tableres.getValueAt(rowsselected[i], colsselected[j]));
                        tableres.setValueAt(null, rowsselected[i], colsselected[j]);
                        if (j < numcols - 1) {
                            sbf.append('\t');
                        }
                    }
                    sbf.append('\n');
                }
                stsel = new StringSelection(sbf.toString());
                clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stsel, stsel);
            }
            if (men.getText().equals("Borrar")) {
                for (int i = resumen.getRowCount(); i > 0; --i) {
                    resumen.removeRow(i - 1);
                }
                Interfazmac.tresu.setComponentPopupMenu(null);
                Interfazmac.guardarima.setEnabled(false);
                nc = 0;
            }
            if (men.getText().equals("Guardar")) {
                guardaResumen();
                GuardarSalida fileSaveres = new GuardarSalida(lbl, ext, etiqueta, tableres);
                fileSaveres.guardaResumen();
            }
            if (men.getText().equals("Guardar como...")) {
                guardaResumencomo();
            }
            if (men.getText().equals("Renombrar")) {
                renombrar();
            }
        }
    }

    /*
	private void selecccolumna(int rowIndex) {
	// Check limits
	if (rowIndex >= 0 && rowIndex < resumen.getRowCount()) {
	// Select the whole row
	resumen.addRowSelectionInterval(rowIndex, rowIndex);
	resumen.addColumnSelectionInterval(0, resumen.getColumnCount() - 1);
		   
	}
	}
     */
    public void guardaResultado() {
        indicador.setVisible(false);
        JFileChooser chooser = new JFileChooser();
        chooser.setLocale(Locale.getDefault());
        chooser.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, fondopanel));
        //JFileChooser chooser2 = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivo de hoja de calculo", ".xlsx");
        // FileFilter filter = new FileNameExtensionFilter("Archivo delimitado por tabulaciones", "csv");
        chooser.setFileFilter(filter);
        chooser.setDialogTitle("Guardar Resumen Como");
        chooser.setCurrentDirectory(new File(dir));
        dir = chooser.getCurrentDirectory().getPath() + File.separator;
        //int actionDialog = chooser.showOpenDialog(f);
        //chooser.setIconImage((icon).getImage());
        //chooser2.setDialogTitle("Renombrar archivo");
        String extres = ((FileNameExtensionFilter) chooser.getFileFilter()).getExtensions()[0];
        Writer writer = null;
        String xlsFile = dir + "Resumen Conteo %d" + extres;
        File csvFile = null;
        for (int i = 1;; i++) {
            csvFile = new File(String.format(xlsFile, i));
            if (!csvFile.exists()) {
                filename = csvFile.getPath();
                break;
            }
        }

        chooser.setSelectedFile(new File(filename));
        int actionDialog = chooser.showSaveDialog(Interfazmac.myJFrame);

        chooser.setAcceptAllFileFilterUsed(false);

        if (actionDialog == JFileChooser.APPROVE_OPTION) {
            csvFile = new File(chooser.getSelectedFile().toString());
            Workbook workbook = new Workbook(csvFile.getPath(), lbl);
            List<Integer> colnumber = new ArrayList<Integer>();
            //int[] colnumber = new int[15];  
            List<Integer> coltext = new ArrayList<Integer>();
            for (int i = 0; i < modelo.getColumnCount(); i++) {
                workbook.getCurrentWorksheet().addNextCell(String.valueOf(modelo.getColumnName(i).toString().replaceAll("\\<.*?\\>", "")), BasicStyles.Bold());
                //sheet.addCell(new Label(i, 0, String.valueOf(modelo.getColumnName(i)), cellFormats));
                //rowhead.createCell(i).setCellValue(String.valueOf(modelo.getColumnName(i)));  
                //rowhead.getCell(i).setCellStyle(style);
                if (Character.isDigit(modelo.getValueAt(0, i).toString().charAt(0))) {
                    colnumber.add(i);
                    //colnumber[c] = c;  
                } else {
                    coltext.add(i);
                }
            }
            System.out.println("cuantos datos " + colnumber.size());
            //int[] coltext = {0, 9, 10, 12, 20};
            //int[] colnumber = {1, 2, 3, 4, 5, 6, 7, 8, 11, 13, 14, 15, 16, 17, 18, 19};
            for (int i = 0; i < modelo.getRowCount(); i++) {
                //HSSFRow row = sheetx.createRow(i+1); 
                workbook.getCurrentWorksheet().goToNextRow();
                for (int j = 0; j < coltext.size(); j++) {
                    Object valuestr = modelo.getValueAt(i, coltext.get(j));
                    //Cell c =row.createCell(coltext.get(j));
                    //c.setCellValue(String.valueOf(modelo.getValueAt(i, coltext.get(j))));
                    workbook.getCurrentWorksheet().addNextCell(String.valueOf(valuestr));
                    //sheet.addCell(new Label(coltext.get(j), i + 1, String.valueOf(valuestr)));
                }
                for (int j = 0; j < colnumber.size(); j++) {
                    Object valuenumb = modelo.getValueAt(i, colnumber.get(j));
                    //Cell c =row.createCell(colnumber.get(j));
                    //c.setCellValue(Double.parseDouble(String.valueOf(modelo.getValueAt(i, colnumber.get(j)))));
                    workbook.getCurrentWorksheet().addNextCell(Double.parseDouble(String.valueOf(valuenumb)));
                    //sheet.addCell(new Number(colnumber.get(j), i + 1, Double.parseDouble(String.valueOf(valuenumb))));
                }
            }

            if (!csvFile.exists()) {

                try {
                    workbook.save();
                    //OutputStream out = new FileOutputStream(csvFile);
                    //workbook.saveAsStream(out); // Save the workbook as myWorkbook.xlsx
                    //out.close();

                    //w.write();
                    //w.close();
                    //out.close();
                } catch (Exception e) {
                    System.out.println("Falló yuy " + e.toString());
                    JOptionPane pane = new JOptionPane("El sistema no tiene acceso al archivo: " + csvFile.getName() + ",\nporques otro proceso lo tiene ocupado.\n Se requiere cerrar el archivo y volver a intentarlo.",
                            JOptionPane.ERROR_MESSAGE, 0, icon2, new String[]{"Aceptar"}, "Aceptar");
                    JDialog dialog = pane.createDialog(Interfazmac.myJFrame, "Error al guardar el archivo de resultados");
                    dialog.setIconImage((icon).getImage());
                    dialog.setAlwaysOnTop(true);

                    dialog.setVisible(true);
                    return;

                }
            } else if (csvFile.exists()) {
                IJ.showStatus("El archivo " + csvFile.getName() + " ya existe en el directorio");
                Interfazmac.statusBar.setText("El archivo " + csvFile.getName() + " ya existe en el directorio");
                JOptionPane pane = new JOptionPane("El archivo: " + csvFile.getName() + " ya existe en el directorio\n¿Desea guardar el archivo con un nombre diferente?",
                        JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION, icon2, new String[]{"Sobreescribir", "Cancelar"}, "Sobreescribir");
                JDialog dialog = pane.createDialog(Interfazmac.myJFrame, "Error al guardar el archivo de resultados");
                dialog.setIconImage((icon).getImage());
                dialog.setAlwaysOnTop(true); // Asegúrate de que esté siempre encima de la ventana principal
                dialog.setVisible(true);

                if (pane.getValue() == null) {
                    IJ.showStatus("El archivo: " + csvFile.getName() + " no se guardó");
                    Interfazmac.statusBar.setText("El archivo: " + csvFile.getName() + " no se guardó");
                    return;
                } else if (pane.getValue().equals("Sobreescribir")) {

                    for (int i = 0; i < modelo.getRowCount(); i++) {
                        //HSSFRow row = sheetx.createRow(i+1); 
                        workbook.getCurrentWorksheet().goToNextRow();
                        for (int j = 0; j < coltext.size(); j++) {
                            Object valuestr = modelo.getValueAt(i, coltext.get(j));
                            //Cell c =row.createCell(coltext.get(j));
                            //c.setCellValue(String.valueOf(modelo.getValueAt(i, coltext.get(j))));
                            workbook.getCurrentWorksheet().addNextCell(String.valueOf(valuestr));
                            //sheet.addCell(new Label(coltext.get(j), i + 1, String.valueOf(valuestr)));
                        }
                        for (int j = 0; j < colnumber.size(); j++) {
                            Object valuenumb = modelo.getValueAt(i, colnumber.get(j));
                            //Cell c =row.createCell(colnumber.get(j));
                            //c.setCellValue(Double.parseDouble(String.valueOf(modelo.getValueAt(i, colnumber.get(j)))));
                            workbook.getCurrentWorksheet().addNextCell(Double.parseDouble(String.valueOf(valuenumb)));
                            //sheet.addCell(new Number(colnumber.get(j), i + 1, Double.parseDouble(String.valueOf(valuenumb))));
                        }
                    }
                    try {
                        OutputStream out = new FileOutputStream(csvFile);
                        workbook.saveAsStream(out); // Save the workbook as myWorkbook.xlsx
                        out.close();
                    } catch (Exception e) {
                        System.out.println("Falló " + e.toString());
                    }
                } else if (pane.getValue().equals("Cancelar")) {
                    return;
                }
            }
        }
    }

    /*
    public void guardaResultado() {
        WritableWorkbook w = null;
        File csvFile = new File(dir + "Resultados Conteo " + lbl + " " + ext + etiqueta + ".xls");
        if (!csvFile.exists()) {

            try {
                OutputStream out = new FileOutputStream(csvFile);
                w = Workbook.createWorkbook(out);
                WritableSheet sheet = w.createSheet(lbl, 0);

                for (int i = 0; i < modelo.getColumnCount(); i++) {
                    for (int j = 0; j < modelo.getRowCount(); j++) {
                        Object titulo = modelo.getColumnName(i);
                        sheet.addCell(new Label(i, j, String.valueOf(modelo.getColumnName(i))));
                    }
                }

                for (int i = 0; i < modelo.getColumnCount(); i++) {
                    for (int j = 0; j < modelo.getRowCount(); j++) {
                        Object object = modelo.getValueAt(j, i);
                        sheet.addCell(new Label(i, j + 1, String.valueOf(modelo.getValueAt(j, i))));
                        //sheet.addCell(new Number(i, j + 1, Double.parseDouble(String.valueOf(modelo.getValueAt(j, i)))));
                    }

                }
                w.write();
                w.close();
                out.close();
            } catch (Exception e) {
                System.out.println("Falló " + e.toString());
            }
        } else if (csvFile.exists()) {
            IJ.showStatus("El archivo " + csvFile.getName() + " ya existe en el directorio");
            Interfazmac.statusBar.setText("El archivo " + csvFile.getName() + " ya existe en el directorio");
            String[] buttons = {"Sobreescribir", "Cancelar"};
            JOptionPane pane = new JOptionPane("El archivo: " + csvFile.getName() + " ya existe en el directorio  \n  ¿Desea guardar el archivo con un nombre diferente?",
                    JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION, icon2, new String[]{"Sobreescribir", "Cancelar"}, "Sobreescribir");
            JDialog dialog = pane.createDialog(null, "Error al guardar el archivo de resultados");
            dialog.setIconImage((icon).getImage());
            dialog.setAlwaysOnTop(true);

            dialog.setIconImage((icon).getImage());
            dialog.setAlwaysOnTop(true);
            dialog.toFront();
            dialog.setVisible(true);
            if (pane.getValue() == null) {
                IJ.showStatus("El archivo: " + csvFile.getName() + " no se guardó");
                Interfazmac.statusBar.setText("El archivo: " + csvFile.getName() + " no se guardó");
                return;
            } else if (pane.getValue().equals("Sobreescribir")) {
                try {
                    OutputStream out = new FileOutputStream(csvFile);
                    w = Workbook.createWorkbook(out);
                    WritableSheet sheet = w.createSheet("Resultados Conteo " + lbl, 0);

                    for (int i = 0; i < modelo.getColumnCount(); i++) {
                        for (int j = 0; j < modelo.getRowCount(); j++) {
                            Object titulo = modelo.getColumnName(i);
                            sheet.addCell(new Label(i, j, String.valueOf(modelo.getColumnName(i))));
                        }
                    }

                    for (int i = 0; i < modelo.getColumnCount(); i++) {
                        for (int j = 0; j < modelo.getRowCount(); j++) {
                            Object object = modelo.getValueAt(j, i);
                            sheet.addCell(new Label(i, j + 1, String.valueOf(modelo.getValueAt(j, i))));
                            //sheet.addCell(new Number(i, j + 1, Double.parseDouble(String.valueOf(modelo.getValueAt(j, i)))));
                        }

                    }
                    w.write();
                    w.close();
                    out.close();
                } catch (Exception e) {
                    System.out.println("Falló " + e.toString());
                }
            } else if (pane.getValue().equals("Cancelar")) {
                return;
            }
        }
    }
     */
    private void guardaResultados() {
        //UIManager.put("OptionPane.border", new LineBorder(fondopanel, 2));											  
        UIManager.put("RootPane.dialogBorder", new LineBorder(Color.BLUE, 4));

        Writer writer = null;
        File csvFile = new File(dir + "Resultados Conteo " + lbl + " " + ext + etiqueta + ".csv");
        if (!csvFile.exists()) {

            try {
                File f = new File(dir + "Resultados Conteo " + lbl + " " + ext + etiqueta + ".csv");
                OutputStream fichero = new FileOutputStream(f);
                writer = new BufferedWriter(new OutputStreamWriter(fichero, "UTF-8"));
                writer.write('\ufeff');
                StringBuilder bufferHeader = new StringBuilder();
                for (int i = 0; i < modelo.getColumnCount(); i++) {
                    bufferHeader.append(modelo.getColumnName(i));
                    if (j != modelo.getColumnCount()) {
                        bufferHeader.append(", ");
                    }
                }
                writer.write(bufferHeader.toString() + "\r\n");
                for (int i = 0; i < modelo.getRowCount(); i++) {
                    StringBuilder buffer = new StringBuilder();
                    for (int j = 0; j < modelo.getColumnCount(); j++) {
                        buffer.append(modelo.getValueAt(i, j));
                        if (j != modelo.getColumnCount()) {
                            buffer.append(",");
                        }
                    }
                    writer.write(buffer.toString() + "\r\n");
                    IJ.showProgress(19, 20);
                    Interfazmac.progressBar.setValue(19);

                }
                writer.close();
                IJ.showStatus("El archivo: " + csvFile.getName() + " se guardó correctamente");
                timef = timef + time();
                Interfazmac.statusBar.setText("El análisis se realizó en " + nimage + " imágenes, en un tiempo total de: " + dec.format(timef) + " s, con un promedio por imagen de " + dec.format(timef / nimage) + " s");
            } catch (IOException e) {
                e.printStackTrace(System.out);
                JOptionPane pane = new JOptionPane("El archivo: Resultados Conteo " + lbl + " " + ext + etiqueta + " se encuentra abierto\n o en uso por otra aplicación  ¿Desea guardar el archivo con un nombre diferente?",
                        JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION, icon2, new String[]{"Guardar", "Cancelar"}, "Guardar");
                pane.setBorder(new LineBorder(fondopanel, 2));
                JDialog dialog = pane.createDialog(Interfazmac.myJFrame, "Error al guardar el archivo de resultados");
                dialog.setIconImage((icon).getImage());
                dialog.setAlwaysOnTop(true);
                dialog.toFront();
                dialog.setVisible(true);
                //int value = ((Integer) pane.getValue()).intValue();
                if (((Integer) pane.getValue()).intValue() == JOptionPane.YES_OPTION) {
                    guardarArchivo();
                } else {
                    IJ.showStatus("El archivo: " + csvFile.getName() + " no se pudo guardar");
                    Interfazmac.statusBar.setText("El archivo: " + csvFile.getName() + " no se pudo guardar");
                    return;
                }
            }

            Toolkit.getDefaultToolkit().beep();
        } else if (csvFile.exists()) {
            IJ.showStatus("El archivo " + csvFile.getName() + " ya existe en el directorio");
            Interfazmac.statusBar.setText("El archivo " + csvFile.getName() + " ya existe en el directorio");
            JOptionPane pane = new JOptionPane("El archivo: " + csvFile.getName() + " ya existe en el directorio  \n  ¿Desea guardar el archivo con un nombre diferente?",
                    JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION, iconsob, new String[]{"Renombrar", "Sobreescribir"}, "Renombrar");
            JDialog dialog = pane.createDialog(Interfazmac.myJFrame, "Error al guardar el archivo de resultados");
            dialog.setIconImage((icon).getImage());
            dialog.setAlwaysOnTop(true);
            //dialog.toFront();
            dialog.setVisible(true);

            if (pane.getValue() == null) {
                IJ.showStatus("El archivo: " + csvFile.getName() + " no se guardó");
                Interfazmac.statusBar.setText("El archivo: " + csvFile.getName() + " no se guardó");
                return;
            } else if (pane.getValue().equals("Sobreescribir")) {
                try {
                    OutputStream fichero = new FileOutputStream(csvFile);
                    writer = new BufferedWriter(new OutputStreamWriter(fichero, "UTF-8"));
                    writer.write('\ufeff');
                    StringBuilder bufferHeader = new StringBuilder();
                    for (int i = 0; i < modelo.getColumnCount(); i++) {
                        bufferHeader.append(modelo.getColumnName(i));
                        if (i != modelo.getColumnCount()) {
                            bufferHeader.append(", ");
                        }
                    }
                    writer.write(bufferHeader.toString() + "\r\n");
                    for (int i = 0; i < modelo.getRowCount(); i++) {
                        StringBuilder buffer = new StringBuilder();
                        for (int j = 0; j < modelo.getColumnCount(); j++) {
                            buffer.append(modelo.getValueAt(i, j));
                            if (j != modelo.getColumnCount()) {
                                buffer.append(",");
                            }
                        }
                        writer.write(buffer.toString() + "\r\n");

                        IJ.showProgress(19, 20);
                        Interfazmac.progressBar.setValue(0);

                    }
                    writer.close();
                    timef = timef + time();
                    Toolkit.getDefaultToolkit().beep();
                    Interfazmac.statusBar.setText("El archivo " + csvFile.getName() + " se sobrescribió en un tiempo total de: " + dec.format(timef) + " s");

                } catch (IOException e) {
                    e.printStackTrace(System.out);
                    JOptionPane panef = new JOptionPane("El sistema no tiene acceso al archivo: " + csvFile.getName() + ",\n porque otro proceso lo tiene ocupado.\n Guarde el archivo con un nombre diferente, o bien se requiere cerrar el archivo.", JOptionPane.ERROR_MESSAGE);
                    JDialog dialogf = panef.createDialog(Interfazmac.myJFrame, "Error al guardar el archivo de resultados");
                    dialogf.setIconImage((icon).getImage());
                    dialogf.setAlwaysOnTop(true);
                    dialogf.toFront();
                    dialogf.setVisible(true);
                    guardarArchivo();
                    return;
                }

            } else if (pane.getValue().equals("Renombrar")) {
                guardarArchivo();
            }
            IJ.showStatus("El archivo " + csvFile.getName() + " se sobrescribió");

        }

    }

    /*
    public void guardaResumen() {
        indicador.setVisible(false);

        String csvFile = dir + "Resumen Conteo %d.csv";
        File f = null;

        for (int i = 1;; i++) {
            f = new File(String.format(csvFile, i));
            if (!f.exists()) {
                break;
            }
        }

        try {

            FileWriter fichero = new FileWriter(f);

            for (int i = 0; i < resumen.getColumnCount(); i++) {
                fichero.write(resumen.getColumnName(i) + ",");

            }
            fichero.write("\n");
            for (int i = 0; i < resumen.getRowCount(); i++) {
                for (int j = 0; j < resumen.getColumnCount(); j++) {
                    fichero.write(resumen.getValueAt(i, j).toString() + ",");
                }
                fichero.write("\n");
            }
            //fichero.flush(); generar un solo archivo, pero disponible hasta cerrar el programa
            fichero.close();
            IJ.showProgress(20, 20);
        } catch (IOException e) {
            String[] options = {"Aceptar"};
            JOptionPane pane = new JOptionPane("Se requiere cerrar el archivo: Resumen Conteo.", JOptionPane.ERROR_MESSAGE, 0, icon2, options, options[0]);
            JDialog dialog = pane.createDialog(null, "Error al guardar el resumen de los resultados");
            dialog.setIconImage((icon).getImage());
            dialog.setAlwaysOnTop(true);
            dialog.toFront();
            dialog.setVisible(true);
            return;
        }
        Toolkit.getDefaultToolkit().beep();

    }
     */
    public void guardaResumen() {
        Writer writer = null;
        String csvFile = dir + "Resumen Conteo %d.csv";
        File f = null;

        for (int i = 1;; i++) {
            f = new File(String.format(csvFile, i));
            if (!f.exists()) {
                break;
            }
        }
        int Row = resumen.getRowCount();
        int Col = resumen.getColumnCount();
        try {
            OutputStream fichero = new FileOutputStream(f);
            writer = new BufferedWriter(new OutputStreamWriter(fichero, "UTF-8"));
            PrintWriter w = new PrintWriter(new OutputStreamWriter(fichero, "UTF-16"));
            writer.write('\ufeff');

            StringBuilder bufferHeader = new StringBuilder();
            for (int j = 0; j < Col; j++) {
                bufferHeader.append(resumen.getColumnName(j));
                if (j != Col) {
                    bufferHeader.append(", ");
                }
            }
            writer.write(bufferHeader.toString() + "\r\n");

            for (int i = 0; i < Row; i++) {
                StringBuilder buffer = new StringBuilder();
                for (int j = 0; j < Col; j++) {
                    buffer.append(resumen.getValueAt(i, j));
                    if (j != Col) {
                        buffer.append(",");
                    }
                }
                writer.write(buffer.toString() + "\r\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace(System.out);
            JOptionPane pane = new JOptionPane("Se requiere cerrar el archivo: Resumen Conteo.", JOptionPane.ERROR_MESSAGE, 0, icon2, new String[]{"Aceptar"}, "Aceptar");
            JDialog dialog = pane.createDialog(Interfazmac.myJFrame, "Error al guardar el resumen de los resultados");
            dialog.setIconImage((icon).getImage());
            dialog.setAlwaysOnTop(true);
            dialog.toFront();
            dialog.setVisible(true);
            return;

        }

    }

    public void guardaResumencomo() {
        indicador.setVisible(false);
        JFileChooser chooser = new JFileChooser();
        chooser.setLocale(Locale.getDefault());
        chooser.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, fondopanel));
        //JFileChooser chooser2 = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivo delimitado por tabulaciones", ".csv");
        // FileFilter filter = new FileNameExtensionFilter("Archivo delimitado por tabulaciones", "csv");
        chooser.setFileFilter(filter);
        chooser.setDialogTitle("Guardar Resumen Como");
        //int actionDialog = chooser.showOpenDialog(f);
        //chooser.setIconImage((icon).getImage());
        //chooser2.setDialogTitle("Renombrar archivo");
        String extres = ((FileNameExtensionFilter) chooser.getFileFilter()).getExtensions()[0];
        Writer writer = null;
        String csvFile = dir + "Resumen Conteo %d" + extres;
        File fi = null;
        for (int i = 1;; i++) {
            fi = new File(String.format(csvFile, i));
            if (!fi.exists()) {
                filename = fi.getPath();
                break;
            }
        }

        chooser.setSelectedFile(new File(filename));
        int actionDialog = chooser.showSaveDialog(Interfazmac.myJFrame);

        chooser.setAcceptAllFileFilterUsed(false);

        if (actionDialog == JFileChooser.APPROVE_OPTION) {
            File fil = chooser.getSelectedFile();
            try {
                OutputStream fichero = new FileOutputStream(fil);
                writer = new BufferedWriter(new OutputStreamWriter(fichero, "UTF-8"));
                PrintWriter w = new PrintWriter(new OutputStreamWriter(fichero, "UTF-16"));
                writer.write('\ufeff');

                StringBuilder bufferHeader = new StringBuilder();
                for (int i = 0; i < resumen.getColumnCount(); i++) {
                    bufferHeader.append(resumen.getColumnName(i));
                    if (i != resumen.getColumnCount()) {
                        bufferHeader.append(", ");
                    }
                }
                writer.write(bufferHeader.toString() + "\r\n");

                for (int i = 0; i < resumen.getRowCount(); i++) {
                    StringBuilder buffer = new StringBuilder();
                    for (int j = 0; j < resumen.getColumnCount(); j++) {
                        buffer.append(resumen.getValueAt(i, j));
                        if (j != resumen.getColumnCount()) {
                            buffer.append(",");
                        }
                    }
                    writer.write(buffer.toString() + "\r\n");
                }
                writer.close();
                IJ.showProgress(20, 20);
            } catch (IOException e) {
                e.printStackTrace(System.out);
                JOptionPane pane = new JOptionPane("Se requiere cerrar el archivo: Resumen Conteo.", JOptionPane.ERROR_MESSAGE, 0, icon2, new String[]{"Aceptar"}, "Aceptar");
                JDialog dialog = pane.createDialog(Interfazmac.myJFrame, "Error al guardar el resumen de los resultados");
                dialog.setIconImage((icon).getImage());
                dialog.setAlwaysOnTop(true);
                dialog.toFront();
                dialog.setVisible(true);
                return;
            }
        }
        /*
	try {
	FileWriter fichero = new FileWriter(dir+"Resumen Conteo.csv");
	for(int i=0; i <resumen.getColumnCount(); i++) {
            fichero.write(resumen.getColumnName(i) + ",");
	}
        fichero.write("\n");
        for(int i=0; i< resumen.getRowCount(); i++) {
        for(int j=0; j < resumen.getColumnCount(); j++) {
        fichero.write(resumen.getValueAt(i,j).toString()+",");
        }
        fichero.write("\n");
        }
        fichero.close();
        IJ.showProgress(20, 20);
	}catch(IOException e){
	String[] options = {"Aceptar"};
	JOptionPane pane = new JOptionPane("Se requiere cerrar el archivo: Resumen Conteo.", JOptionPane.ERROR_MESSAGE,  0,  icon2, options, options[0]);  
	JDialog dialog = pane.createDialog(null, "Error al guardar el resumen de los resultados");  
	dialog.setIconImage((icon).getImage());
	dialog.setAlwaysOnTop(true);  
	dialog.setVisible(true);  
	return;}
         */
        IJ.showProgress(22, 20);
        IJ.showProgress(22, 22);
        /*
	JOptionPane.showMessageDialog(null,
	"Se requiere cerrar el archivo: Resumen Conteo.",
	"Error al guardar el resumen de los resultados", 
	JOptionPane.ERROR_MESSAGE); return;}
         */
    }

    public void guardaResumenxlsx() {
        indicador.setVisible(false);
        JFileChooser chooser = new JFileChooser();
        chooser.setLocale(Locale.getDefault());
        chooser.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, fondopanel));
        //JFileChooser chooser2 = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivo de hoja de calculo", ".xls");
        // FileFilter filter = new FileNameExtensionFilter("Archivo delimitado por tabulaciones", "csv");
        chooser.setFileFilter(filter);
        chooser.setDialogTitle("Guardar Resumen Como");
        chooser.setCurrentDirectory(new File(dir));
        dir = chooser.getCurrentDirectory().getPath() + File.separator;
        //int actionDialog = chooser.showOpenDialog(f);
        //chooser.setIconImage((icon).getImage());
        //chooser2.setDialogTitle("Renombrar archivo");
        String extres = ((FileNameExtensionFilter) chooser.getFileFilter()).getExtensions()[0];
        Writer writer = null;
        String xlsFile = dir + "Resumen Conteo %d" + extres;
        File csvFile = null;
        for (int i = 1;; i++) {
            csvFile = new File(String.format(xlsFile, i));
            if (!csvFile.exists()) {
                filename = csvFile.getPath();
                break;
            }
        }

        chooser.setSelectedFile(new File(filename));
        int actionDialog = chooser.showSaveDialog(Interfazmac.myJFrame);

        chooser.setAcceptAllFileFilterUsed(false);

        if (actionDialog == JFileChooser.APPROVE_OPTION) {
            csvFile = new File(chooser.getSelectedFile().toString());

            WritableWorkbook w = null;
            if (!csvFile.exists()) {

                try {

                    //jxl.Workbook w = jxl.Workbook.getWorkbook(csvFile);
                    w = jxl.Workbook.createWorkbook(csvFile);
                    //Sheet sheet = w.getSheet(0);
                    WritableSheet sheet = w.createSheet(lbl, 0);
                    //nombre de columnas
                    WritableFont cellFonts = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
                    WritableCellFormat cellFormats = new WritableCellFormat(cellFonts);
                    //cellFormats.setWrap(true);
                    cellFormats.setAlignment(jxl.format.Alignment.CENTRE);
                    //cellFormats.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.AUTOMATIC);
                    //cellFormats.setBackground(Colour.WHITE);
                    Double tpgx = (double) System.currentTimeMillis();
                    //la diferencia entre los siguientes metodos es minima, al parecer es ligeramente menor con este primero 0.08s
                    /*
                    List<Integer> colnumber = new ArrayList<Integer>(); 
                    List<Integer> coltext = new ArrayList<Integer>();
                    for (int i = 0; i < modelo.getColumnCount(); i++) {
                        sheet.addCell(new Label(i, 0, String.valueOf(modelo.getColumnName(i)), cellFormats));
                        if (Character.isDigit(modelo.getValueAt(0, i).toString().charAt(0))) {
                            colnumber.add(i);
                        } else {
                            coltext.add(i);
                        }
                    }
                    //System.out.println("cuantos datos " + colnumber.size());
                    for (int i = 0; i < modelo.getRowCount(); i++) {
                        for (int j = 0; j < coltext.size(); j++) {
                            //Object valuestr = modelo.getValueAt(i, coltext.get(j));
                            sheet.addCell(new Label(coltext.get(j), i + 1, String.valueOf(modelo.getValueAt(i, coltext.get(j)))));
                        }
                        for (int j = 0; j < colnumber.size(); j++) {
                            //Object valuenumb = modelo.getValueAt(i, colnumber.get(j));
                            sheet.addCell(new Number(colnumber.get(j), i + 1, Double.parseDouble(String.valueOf(modelo.getValueAt(i, colnumber.get(j))))));
                        }
                    }
                     */
                    //int[][] colnumber;

                    for (int j = 0; j < modelo.getColumnCount(); j++) {
                        sheet.addCell(new Label(j, 0, String.valueOf(modelo.getColumnName(j)), cellFormats));
                        //String hyt=(Character.isDigit(modelo.getValueAt(0, j).toString().charAt(0))break;) ? "si":"no;"
                        //System.out.println("columnas numericas " + colnumber[0].length+ " columnas texto " +colnumber[1].length);
                        for (int i = 0; i < modelo.getRowCount(); i++) {
                            //char ch = modelo.getValueAt(0, j).toString().charAt(0);
                            WritableCell valuenumb = (Character.isDigit(modelo.getValueAt(0, j).toString().charAt(0))) ? new Number(j, i + 1, Double.parseDouble(String.valueOf(modelo.getValueAt(i, j)))) : new Label(j, i + 1, String.valueOf(modelo.getValueAt(i, j)));
                            System.out.println("columnas numericas " + valuenumb.getContents());
                            sheet.addCell(valuenumb);
                        }

                    }

                    System.out.println("Tiempo " + (System.currentTimeMillis() - tpgx) / 1000.0D);
                    w.write();
                    w.close();
                } catch (Exception e) {
                    System.out.println("Falló " + e.toString());
                    JOptionPane pane = new JOptionPane("El sistema no tiene acceso al archivo: " + csvFile.getName() + ",\nporques otro proceso lo tiene ocupado.\n Se requiere cerrar el archivo y volver a intentarlo.", JOptionPane.ERROR_MESSAGE, 0, icon2, new String[]{"Aceptar"}, "Aceptar");
                    JDialog dialog = pane.createDialog(null, "Error al guardar el archivo de resultados");
                    dialog.setIconImage((icon).getImage());
                    dialog.setAlwaysOnTop(true);
                    dialog.toFront();
                    dialog.setVisible(true);
                    return;

                }
            } else if (csvFile.exists()) {
                /*
                xlsFile = dir + "Resumen Conteo %d" + extres;
                csvFile = null;
                for (int i = 1;; i++) {
                    csvFile = new File(String.format(xlsFile, i));
                    if (!csvFile.exists()) {
                        filename = csvFile.getPath();
                        break;
                    }
                }
                 */
                IJ.showStatus("El archivo " + csvFile.getName() + " ya existe en el directorio");
                Interfazmac.statusBar.setText("El archivo " + csvFile.getName() + " ya existe en el directorio");
                JOptionPane pane = new JOptionPane("El archivo: " + csvFile.getName() + " ya existe en el directorio  \n  ¿Desea guardar el archivo con un nombre diferente?",
                        JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION, icon2, new String[]{"Renombrar", "Sobreescribir"}, "Renombrar");
                JDialog dialog = pane.createDialog(Interfazmac.myJFrame, "Error al guardar el archivo de resultados");
                dialog.setIconImage((icon).getImage());
                dialog.setAlwaysOnTop(true);

                dialog.setIconImage((icon).getImage());
                dialog.setAlwaysOnTop(true);
                dialog.toFront();
                dialog.setVisible(true);
                if (pane.getValue() == null) {
                    IJ.showStatus("El archivo: " + csvFile.getName() + " no se guardó");
                    Interfazmac.statusBar.setText("El archivo: " + csvFile.getName() + " no se guardó");
                    return;
                } else if (pane.getValue().equals("Sobreescribir")) {
                    try {
                        //OutputStream out = new FileOutputStream(csvFile);
                        w = jxl.Workbook.createWorkbook(csvFile);
                        WritableSheet sheet = w.createSheet(lbl, 0);
                        WritableFont cellFonts = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
                        WritableCellFormat cellFormats = new WritableCellFormat(cellFonts);
                        //cellFormats.setWrap(true);
                        cellFormats.setAlignment(jxl.format.Alignment.CENTRE);
                        for (int j = 0; j < modelo.getColumnCount(); j++) {
                            sheet.addCell(new Label(j, 0, String.valueOf(modelo.getColumnName(j)), cellFormats));
                            for (int i = 0; i < modelo.getRowCount(); i++) {
                                //char ch = modelo.getValueAt(0, j).toString().charAt(0);
                                WritableCell valuenumb = (Character.isDigit(modelo.getValueAt(0, j).toString().charAt(0))) ? new Number(j, i + 1, Double.parseDouble(String.valueOf(modelo.getValueAt(i, j)))) : new Label(j, i + 1, String.valueOf(modelo.getValueAt(i, j)));
                                sheet.addCell(valuenumb);
                            }

                        }
                        w.write();
                        w.close();
                        //out.close();
                    } catch (Exception e) {
                        System.out.println("Falló " + e.toString());
                    }
                } else if (pane.getValue().equals("Cancelar")) {
                    return;
                }
            }
        }
    }

    public void guardaResumenxls() {
        /*
        Workbook workbooks = new Workbook(dir + "test3.xlsx", "Sheet1"); // Para el guardado automático sin filechooser
                try {
            workbooks.save();                                                    // Save the workbook
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
         */
        indicador.setVisible(false);
        chooser = new JFileChooser();
        chooser.setLocale(Locale.getDefault());
        chooser.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, fondopanel));
        //JFileChooser chooser2 = new JFileChooser();
        String[] extensiones = {".xlsx", ".csv"};

        chooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter File_ext_csv = new FileNameExtensionFilter("Archivo delimitado por tabulaciones", "csv");
        FileNameExtensionFilter File_ext_ods = new FileNameExtensionFilter("Hoja de  de cálculo de OpenDocument", "ods");
        FileNameExtensionFilter File_ext_xlsx = new FileNameExtensionFilter("Libro de Excel 2007-2013", "xlsx");
        chooser.setFileFilter(File_ext_csv);
        chooser.setFileFilter(File_ext_ods);
        chooser.setFileFilter(File_ext_xlsx);;
        chooser.addPropertyChangeListener(this);

        //FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivo de hoja de calculo", ".xlsx", ".csv");
        // FileFilter filter = new FileNameExtensionFilter("Archivo delimitado por tabulaciones", "csv");
        //chooser.setFileFilter(filter);
        chooser.setDialogTitle("Guardar Resumen Como");
        chooser.setCurrentDirectory(new File(dir));
        dir = chooser.getCurrentDirectory().getPath() + File.separator;
        //int actionDialog = chooser.showOpenDialog(f);
        //chooser.setIconImage((icon).getImage());
        //chooser2.setDialogTitle("Renombrar archivo");
        //String[] extres = ((FileNameExtensionFilter)chooser.getFileFilter()).getExtensions();

        //String extres = ((FileNameExtensionFilter) chooser.getFileFilter()).getExtensions()[0];
        //xlsFile = (chooser.getFileFilter().getDescription().equals("Documento de Excel 2007-2013"))?  dir + "Resumen Conteo %d."+((FileNameExtensionFilter) chooser.getFileFilter()).getExtensions()[0]: dir + "Resumen Conteo %d."+((FileNameExtensionFilter) chooser.getFileFilter()).getExtensions()[1];
        //int ntext = (chooser.getFileFilter().getDescription().equals("Documento de Excel 2007-2013"))? 0 : 1;
        String extres = ((FileNameExtensionFilter) chooser.getFileFilter()).getExtensions()[0];
        xlsFile = dir + "Resumen Conteo %d." + extres;
        //chooser.setSelectedFile(new File(xlsFile));
        csvFile = null;
        for (int i = 1;; i++) {
            csvFile = new File(String.format(xlsFile, i));
            if (!csvFile.exists()) {
                filename = csvFile.getPath();
                break;
            }
        }

        chooser.setSelectedFile(new File(filename));
        int actionDialog = chooser.showSaveDialog(Interfazmac.myJFrame);

        if (actionDialog == JFileChooser.CANCEL_OPTION) {
            IJ.showStatus("Se canceló el  guardar el archivo: " + csvFile.getName());
            Interfazmac.statusBar.setText("Se canceló el  guardar el archivo: " + csvFile.getName());
            return;
        }

        if (actionDialog == JFileChooser.APPROVE_OPTION && csvFile.getName().endsWith("xlsx")) {
            //csvFile = (chooser.getFileFilter().getDescription().equals("Documento de Excel 2007-2013"))? new File(chooser.getSelectedFile().toString()+".xlsx"):new File(chooser.getSelectedFile().toString()+".csv");
            //csvFile = Juntos.getSelectedFileWithExtension(chooser);
            csvFile = chooser.getSelectedFile();

            Workbook workbook = new Workbook(csvFile.getPath(), lbl);
            //Workbook workbook = new Workbook(lbl);
            workbook.getWorkbookMetadata().setCreator(System.getProperty("user.name"));
            workbook.getWorkbookMetadata().setTitle("Resultados obtenidos con Macgrain-IJ " + lbl);
            workbook.getWorkbookMetadata().setApplication("Macgrain-IJ " + MACGIJVERSION);
            workbook.getWorkbookMetadata().setCompany("Facultad de Agricultura del valle del Fuerte-Universidad Autónoma de Sinaloa");
            Style s3 = new Style();                                      // Create a style from a predefined style
            //s3.getCellXf().setTextRotation(45);                                 // Set text rotation
            s3.getCellXf().setHorizontalAlign(CellXf.HorizontalAlignValue.center);  // Set alignment
            //workbook.getCurrentWorksheet().getCells().get("A1").setStyle(s3);
            workbook.getCurrentWorksheet().setActiveStyle(s3);
            //workbook.getCurrentWorksheet().setDefaultColumnWidth(18f);

            for (int i = 0; i < modelo.getColumnCount(); i++) {
                workbook.getCurrentWorksheet().addNextCell(String.valueOf(modelo.getColumnName(i).replaceAll("\\<.*?\\>", "")), BasicStyles.font("Arial", 11, true));
                workbook.getCurrentWorksheet().setColumnWidth(i, 17.6f);

            }

            for (int i = 0; i < modelo.getRowCount(); i++) {
                workbook.getCurrentWorksheet().goToNextRow();
                for (int j = 0; j < modelo.getColumnCount(); j++) {
                    char ch = modelo.getValueAt(0, j).toString().charAt(0);
                    Object valuenumb = (ch >= '0' && ch <= '9') ? Double.parseDouble(String.valueOf(modelo.getValueAt(i, j))) : String.valueOf(modelo.getValueAt(i, j));
                    workbook.WS.value(valuenumb); //metodo abreviado
                    //workbook.getCurrentWorksheet().addNextCell(valuenumb);   
                }

            }
            if (!csvFile.exists()) {
                try {

                    workbook.save();
                    //TimeZone tz = TimeZone.getDefault();
                    //Calendar cal = Calendar.getInstance(tz);
                    //Path p = Paths.get(csvFile.getPath());
                    //Files.setAttribute(p, "creationTime", FileTime.fromMillis(cal.getTimeInMillis()));

                    //OutputStream out = new FileOutputStream(csvFile);
                    //workbook.saveAsStream(out); // Save the workbook as myWorkbook.xlsx
                    //out.close();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.out.println("Falló: " + e.getMessage());
                    String msg = "El sistema no pudo guardar el archivo: " + csvFile.getName() + ", es posible que otro proceso lo tenga ocupado. Se requiere cerrar el archivo y volver a intentarlo.";
                    JOptionPane pane = new JOptionPane(getMessage(msg, 400), JOptionPane.ERROR_MESSAGE, 0, icon2, new String[]{"Aceptar"}, "Aceptar");
                    //JOptionPane pane = new JOptionPane("El sistema no pudo guardar el archivo: " + csvFile.getName() + ",\nes posible que otro proceso lo tenga ocupado.\n Se requiere cerrar el archivo y volver a intentarlo.", JOptionPane.ERROR_MESSAGE, 0, icon2, options, options[0]);
                    JDialog dialog = pane.createDialog(Interfazmac.myJFrame, "Error al guardar el archivo de resultados");
                    dialog.setIconImage((icon).getImage());
                    dialog.setAlwaysOnTop(true);
                    dialog.toFront();
                    dialog.setVisible(true);
                    return;

                }
            } else if (csvFile.exists()) {

                IJ.showStatus("El archivo " + csvFile.getName() + " ya existe en el directorio");
                Interfazmac.statusBar.setText("El archivo " + csvFile.getName() + " ya existe en el directorio");
                String msg = "El archivo: " + csvFile.getName() + " ya existe en el directorio ¿Desea guardar el archivo con un nombre diferente?";
                JOptionPane pane = new JOptionPane(getMessage(msg, 400), JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION, icon2, new String[]{"Renombrar", "Sobreescribir"}, "Renombrar");
                // JOptionPane pane = new JOptionPane("El archivo: " + csvFile.getName() + " ya existe en el directorio  \n  ¿Desea guardar el archivo con un nombre diferente?",
                //JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION, icon2, buttons);
                JDialog dialog = pane.createDialog(Interfazmac.myJFrame, "Error al guardar el archivo de resultados");
                dialog.setIconImage((icon).getImage());
                dialog.setAlwaysOnTop(true);
                dialog.toFront();
                dialog.setVisible(true);
                if (pane.getValue() == null) {
                    IJ.showStatus("El archivo: " + csvFile.getName() + " no se guardó");
                    Interfazmac.statusBar.setText("El archivo: " + csvFile.getName() + " no se guardó");
                    return;
                } else if (pane.getValue().equals("Sobreescribir")) {
                    /*
                    workbook = new Workbook(csvFile.getPath(), lbl);
                    //workbook = new Workbook(lbl); 

                    for (int i = 0; i < modelo.getColumnCount(); i++) {
                        workbook.getCurrentWorksheet().addNextCell(String.valueOf(modelo.getColumnName(i).toString().replaceAll("\\<.*?\\>", "")), BasicStyles.font("Dialog", 10, true));
                    }

                    for (int i = 0; i < modelo.getRowCount(); i++) {
                        workbook.getCurrentWorksheet().goToNextRow();
                        for (int j = 0; j < modelo.getColumnCount(); j++) {
                            Object valuenumb = (Character.isDigit(modelo.getValueAt(0, j).toString().charAt(0)))? 
                                    Double.parseDouble(String.valueOf(modelo.getValueAt(i, j))) : String.valueOf(modelo.getValueAt(i, j));
                            workbook.getCurrentWorksheet().addNextCell(valuenumb);
                        }
                    }
                     */
                    try {
                        workbook.save();
                        //OutputStream out = new FileOutputStream(csvFile);
                        //workbook.saveAsStream(out); // Save the workbook as myWorkbook.xlsx
                        //out.close();

                    } catch (Exception e) {
                        System.out.println("Falló sobreescribir " + e.toString());
                        JOptionPane sopane = new JOptionPane("El sistema no tiene acceso al archivo: " + csvFile.getName() + ",\nporques otro proceso lo tiene ocupado.\n Se requiere cerrar el archivo y volver a intentarlo.", JOptionPane.ERROR_MESSAGE, 0, icon2, new String[]{"Aceptar"}, "Aceptar");
                        JDialog sodialog = sopane.createDialog(null, "Error al guardar el archivo de resultados");
                        sodialog.setIconImage((icon).getImage());
                        sodialog.setAlwaysOnTop(true);
                        sodialog.toFront();
                        sodialog.setVisible(true);
                        return;
                    }
                } else if (pane.getValue().equals("Renombrar")) {
                    renombrar();
                }
            }
        } else if (actionDialog == JFileChooser.APPROVE_OPTION && csvFile.getName().endsWith("ods")) {
            //System.out.println("Aqui se seleccionó la extension "+ntext);
            //csvFile = (chooser.getFileFilter().getDescription().equals("Documento de Excel 2007-2013"))? new File(chooser.getSelectedFile().toString()+".xlsx"):new File(chooser.getSelectedFile().toString()+".csv");
            //csvFile = Juntos.getSelectedFileWithExtension(chooser);
            csvFile = chooser.getSelectedFile();
            OdsFactory odsFactory = OdsFactory.create(Logger.getLogger("hello-world"), Locale.getDefault());
            AnonymousOdsFileWriter writerods = odsFactory.createWriter();
            OdsDocument docods = writerods.document();

            //TextStyle boldStyle = TextStyle.builder("bold").fontWeightBold().build();
            TableRowStyle rowStyle = TableRowStyle.builder("row").rowHeight(SimpleLength.pt(15)).build();
            TableCellStyle HEAD_CELL_STYLE
                    = TableCellStyle.builder("Head").verticalAlign(VerticalAlign.MIDDLE).textAlign(CellAlign.CENTER).fontWrap(false)
                            .backgroundColor(SimpleColor.NONE).fontWeightBold()
                            .fontName("Calibri").parentCellStyle(null).build();
            TableCellStyle DEFAULT_CELL_STYLE
                    = TableCellStyle.builder("Default").verticalAlign(VerticalAlign.MIDDLE).textAlign(CellAlign.CENTER).fontWrap(false)
                            .backgroundColor(SimpleColor.NONE).allMargins(Length.NULL_LENGTH).fontStyleNormal()
                            .fontName(new Font("Calibri", Font.PLAIN, 11).toString()).parentCellStyle(null).build();
            lbl = (lbl.length() <= 28) ? lbl : lbl.substring(0, 28) + "...";

            try {
                Table tableods = docods.addTable(lbl);
                for (int i = 0; i < modelo.getColumnCount(); i++) {
                    TableRowImpl rowshead = tableods.getRow(0);

                    com.github.jferard.fastods.TableCell cells = rowshead.getOrCreateCell(i);
                    cells.setStringValue(String.valueOf(modelo.getColumnName(i).replaceAll("\\<.*?\\>", "")));
                    cells.setStyle(HEAD_CELL_STYLE);
                    rowshead.setRowStyle(rowStyle);
                }

                for (int i = 0; i < modelo.getRowCount(); i++) {
                    TableRowImpl rows = tableods.getRow(i + 1);
                    for (int j = 0; j < modelo.getColumnCount(); j++) {
                        com.github.jferard.fastods.TableCell cells = rows.getOrCreateCell(j);
                        cells.setStyle(DEFAULT_CELL_STYLE);
                        if (Character.isLetter(modelo.getValueAt(0, j).toString().charAt(0))) {
                            cells.setStringValue(modelo.getValueAt(i, j).toString());

                        } else {
                            cells.setFloatValue(Float.parseFloat(String.valueOf(modelo.getValueAt(i, j))));
                        }
                    }
                    rows.setRowStyle(rowStyle);
                }
            } catch (Exception e) {
                System.out.println("Falló  " + e.toString());
                JOptionPane pane = new JOptionPane("Falló la integración de los datos en el archivo" + csvFile.getName() + ",\n Se requiere cerrar el archivo y volver a intentarlo.", JOptionPane.ERROR_MESSAGE, 0, icon2, new String[]{"Aceptar"}, "Aceptar");
                JDialog dialog = pane.createDialog(Interfazmac.myJFrame, "Error de integración de los resultados");
                dialog.setIconImage((Interfazmac.icon).getImage());
                dialog.setAlwaysOnTop(true);
                dialog.toFront();
                dialog.setVisible(true);
                return;

            }
            if (!csvFile.exists()) {
                try {
                    writerods.saveAs(csvFile);

                    //writerods.saveAs(new File(dir, titresumen +".ods"));
                } catch (Exception e) {
                    JOptionPane pane = new JOptionPane("El sistema no tiene acceso al archivo: " + csvFile.getName() + ",\nporques otro proceso lo tiene ocupado.\n Se requiere cerrar el archivo y volver a intentarlo.", JOptionPane.ERROR_MESSAGE, 0, icon2, new String[]{"Aceptar"}, "Aceptar");
                    JDialog dialog = pane.createDialog(Interfazmac.myJFrame, "Error al guardar el archivo de resultados");
                    dialog.setIconImage((Interfazmac.icon).getImage());
                    dialog.setAlwaysOnTop(true);
                    dialog.toFront();
                    dialog.setVisible(true);
                    return;
                }
            } else {

                IJ.showStatus("El archivo " + csvFile.getName() + " ya existe en el directorio");
                Interfazmac.statusBar.setText("El archivo " + csvFile.getName() + " ya existe en el directorio");
                String[] buttons = {"Renombrar", "Sobreescribir"};
                JOptionPane pane = new JOptionPane("El archivo: " + csvFile.getName() + " ya existe en el directorio  \n  ¿Desea guardar el archivo con un nombre diferente?",
                        JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION, icon2, buttons);
                JDialog dialog = pane.createDialog(Interfazmac.myJFrame, "Error al guardar el archivo de resultados");
                dialog.setIconImage((Interfazmac.icon).getImage());
                dialog.setAlwaysOnTop(true);
                dialog.toFront();
                dialog.setVisible(true);
                if (pane.getValue() == null) {
                    IJ.showStatus("El archivo: " + csvFile.getName() + " no se guardó");
                    Interfazmac.statusBar.setText("El archivo: " + csvFile.getName() + " no se guardó");
                    return;
                } else if (pane.getValue().equals("Sobreescribir")) {
                    try {
                        writerods.saveAs(csvFile);
                        //writerods.saveAs(new File(dir, titresumen +".ods"));
                    } catch (Exception e) {
                        System.out.println("Falló " + e.toString());
                    }
                } else if (pane.getValue().equals("Renombrar")) {
                    return;
                }
            }

        } else {
            File fil = chooser.getSelectedFile();
            try {
                OutputStream fichero = new FileOutputStream(fil);
                Writer writer = new BufferedWriter(new OutputStreamWriter(fichero, "UTF-8"));
                PrintWriter w = new PrintWriter(new OutputStreamWriter(fichero, "UTF-16"));
                writer.write('\ufeff');

                StringBuilder bufferHeader = new StringBuilder();
                for (int i = 0; i < modelo.getColumnCount(); i++) {
                    bufferHeader.append(modelo.getColumnName(i));
                    if (i != modelo.getColumnCount()) {
                        bufferHeader.append(", ");
                    }
                }
                writer.write(bufferHeader.toString() + "\r\n");

                for (int i = 0; i < modelo.getRowCount(); i++) {
                    StringBuilder buffer = new StringBuilder();
                    for (int j = 0; j < modelo.getColumnCount(); j++) {
                        buffer.append(modelo.getValueAt(i, j));
                        if (j != modelo.getColumnCount()) {
                            buffer.append(",");
                        }
                    }
                    writer.write(buffer.toString() + "\r\n");
                }
                writer.close();
                IJ.showProgress(20, 20);
            } catch (IOException e) {
                e.printStackTrace(System.out);
                String msg = "Se requiere cerrar el archivo: Resumen Conteo.";
                JOptionPane pane = new JOptionPane(getMessage(msg, 400), JOptionPane.ERROR_MESSAGE, 0, icon2, new String[]{"Aceptar"}, "Aceptar");
                //JOptionPane pane = new JOptionPane("Se requiere cerrar el archivo: Resumen Conteo.", JOptionPane.ERROR_MESSAGE, 0, icon2, options, options[0]);
                JDialog dialog = pane.createDialog(Interfazmac.myJFrame, "Error al guardar el resumen de los resultados");
                dialog.setIconImage((icon).getImage());
                dialog.setAlwaysOnTop(true);
                dialog.toFront();
                dialog.setVisible(true);
                return;
            }
        }
    }

    public void renombrar() {

        JComboBox myComboBox = new JComboBox();
        myComboBox.setPreferredSize(new Dimension(280, 22));
        myComboBox.setBorder(new LineBorder(fondopanel));
        File folder = new File(dir);
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles.length == 0) {
            String msg = "No se pudo completar el cambio de nombre del archivo. Existe un otro archivo con el mismo nombre";
            JOptionPane paneb = new JOptionPane(getMessage(msg, 400), JOptionPane.ERROR_MESSAGE, 0, icon2, new String[]{"Aceptar"}, "Aceptar");
            //JOptionPane paneb = new JOptionPane("No se pudo completar el cambio de nombre del archivo.\n Existe un otro archivo con el mismo nombre", JOptionPane.ERROR_MESSAGE, 0, icon2, options, options[0]);
            JDialog dialogb = paneb.createDialog(Interfazmac.myJFrame, "Error al renombrar el archivo");
            dialogb.setIconImage((icon).getImage());
            dialogb.setAlwaysOnTop(true);
            dialogb.toFront();
            dialogb.setVisible(true);
            return;
        }
        /*
	Arrays.sort(listOfFiles, new Comparator<File>() {
        public int compare(File f1, File f2) {
        return Long.compare(f2.lastModified(), f1.lastModified());
        }
	});
         */
        //IJ.run("Compiler...", "target=1.8");
        Arrays.sort(listOfFiles, Comparator.comparingLong(File::lastModified).reversed());
        //Arrays.sort(listOfFiles, (a, b) -> Long.compare(b.lastModified(), a.lastModified())); //compilar en java 8
        for (int fil = 0; fil < listOfFiles.length; fil++) {
            //String exten = listOfFiles[fil].getName().substring(listOfFiles[fil].getName().lastIndexOf("/") + 1, listOfFiles[fil].getName().lastIndexOf("."));
            if (listOfFiles[fil].isFile()) {
                if (listOfFiles[fil].getName().endsWith(".csv")) {
                    myComboBox.addItem(listOfFiles[fil].getName().substring(listOfFiles[fil].getName().lastIndexOf("/") + 1, listOfFiles[fil].getName().lastIndexOf(".")));
                }
            }
        }

        String csvFilesi = (String) myComboBox.getSelectedItem();

        //String csvFiles = csvFilesi.substring(0, csvFilesi.lastIndexOf("."));
        //String csvFiles = "Resumen Conteo";
        int endIndex = csvFilesi.lastIndexOf(".");
        JPanel panfield = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel oldfield = new JLabel("Seleccione el archivo a renombrar :");
        JLabel newfield = new JLabel("Escriba el nuevo nombre :");
        JTextField field = new JTextField(csvFilesi);
        field.setToolTipText("<html>Si deja el nombre original el contador incrementará<br>o disminuirá. El nombre no debe quedar vacío.</html>");
        field.setPreferredSize(new Dimension(280, 25));
        field.setBorder(new LineBorder(fondopanel));
        panfield.add(oldfield);
        panfield.add(myComboBox);
        panfield.add(newfield);
        panfield.add(field);
        if (endIndex > 0) {
            field.setSelectionStart(0);
            field.setSelectionEnd(endIndex);

        } else {
            field.selectAll();
            field.setText(myComboBox.getSelectedItem().toString());
        }
        Object[] msg = {"Escriba el nuevo nombre :", field};
        JOptionPane pane = new JOptionPane(panfield,
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION,
                icong);
        // Use it's own dialog creation process, it's simpler this way
        JDialog dialog = pane.createDialog("Renombrar nombre del cuadro de resumen");
        // Put it on the screen...
        dialog.setIconImage((icon).getImage());
        dialog.pack();
        dialog.setSize(365, 165);
        dialog.setLocationRelativeTo(Interfazmac.myJFrame);
        dialog.setVisible(true);
        dialog.dispose();
        if (pane.getValue() == null) {
            IJ.showStatus("El archivo: " + (String) myComboBox.getSelectedItem() + ".csv" + " no se renombró");
            Interfazmac.statusBar.setText("El archivo: " + (String) myComboBox.getSelectedItem() + ".csv" + " no se renombró");
            return;
        }

        if ((Integer) pane.getValue() == JOptionPane.YES_OPTION) {
            String csvFile = dir + field.getText() + ".csv";
            File file = new File(dir + (String) myComboBox.getSelectedItem() + ".csv");

            //lbl = field.getText().substring("123456789".indexOf(field.getText()), 0);
            //System.out.println(field.getText().replaceAll("[0-9]",""));
            if (field.getText().length() == 0) {
                String msgi = "Se requiere introducir el nuevo nombre del archivo. El espacio no debe quedar en blanco";
                JOptionPane paneb = new JOptionPane(getMessage(msgi, 400), JOptionPane.ERROR_MESSAGE, 0, icon2, new String[]{"Aceptar"}, "Aceptar");
                JDialog dialogb = paneb.createDialog(Interfazmac.myJFrame, "Nombre del archivo vacío");
                dialogb.setIconImage((icon).getImage());
                dialogb.setAlwaysOnTop(true);
                dialogb.setLocationRelativeTo(Interfazmac.myJFrame);
                dialogb.toFront();
                dialogb.setVisible(true);
                renombrar();
                return;
            }

            File newF = new File(dir + field.getText() + ".csv");
            if (newF.exists()) {
                String newFile = dir + field.getText().replaceAll("[0-9]", "") + "%d.csv";
                for (int in = 1;; in++) {
                    File newFi = new File(String.format(newFile, in));
                    if (!newFi.exists()) {
                        file.renameTo(newFi);
                        break;
                    }
                    /*
                        if (!succeeded) {
                                String msgi = "No se realizó el cambio de nombre del archivo porque no se introdujo ninguna modificación. El nombre del archivo permanecerá igual.";
                                JOptionPane.showMessageDialog(myJFrame, getMessage(msgi, 400));
                                return;
                            }
                       
                        String newNameg = dir + newName + " %d.csv";
                        File newG = new File(String.format(newNameg, in));
                        if (!newG.exists()) {
                            file.renameTo(newG);
                            break;
                        }
                     */
                }
            } else {
                file.renameTo(newF);
            }

        }
    }

    private void guardarArchivo() {
        IJ.showStatus("Guardar el archivo de resultados con un nombre diferente");
        Interfazmac.statusBar.setText("Guardar el archivo de resultados con un nombre diferente");
        JFileChooser chooser = new JFileChooser();
        chooser.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, fondopanel));
        //JFileChooser chooser2 = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivo delimitado por tabulaciones", ".csv");
        // FileFilter filter = new FileNameExtensionFilter("Archivo delimitado por tabulaciones", "csv");
        chooser.setFileFilter(filter);
        chooser.setDialogTitle("Guardar Resultados");
        //chooser2.setDialogTitle("Renombrar archivo");
        String extres = ((FileNameExtensionFilter) chooser.getFileFilter()).getExtensions()[0];
        String csvFile = dir + "Resultados Conteo " + lbl + " " + ext + etiqueta + " %d" + extres;
        Writer writer = null;
        File file = null;
        for (int i = 1;; i++) {
            file = new File(String.format(csvFile, i));
            if (!file.exists()) {
                filename = file.getPath();
                break;

            }
        }
        chooser.setSelectedFile(new File(filename));
        int actionDialog = chooser.showSaveDialog(Interfazmac.myJFrame);

        chooser.setAcceptAllFileFilterUsed(false);

        if (actionDialog == JFileChooser.APPROVE_OPTION) {
            File fi = chooser.getSelectedFile();
            try {
                //FileWriter fichero = new FileWriter(fi);
                OutputStream fichero = new FileOutputStream(fi);

                writer = new BufferedWriter(new OutputStreamWriter(fichero, "UTF-8"));
                PrintWriter w = new PrintWriter(new OutputStreamWriter(fichero, "UTF-16"));
                writer.write('\ufeff');

                StringBuilder bufferHeader = new StringBuilder();
                for (int i = 0; i < modelo.getColumnCount(); i++) {
                    bufferHeader.append(modelo.getColumnName(i));
                    if (i != modelo.getColumnCount()) {
                        bufferHeader.append(", ");
                    }
                }
                writer.write(bufferHeader.toString() + "\r\n");
                for (int i = 0; i < modelo.getRowCount(); i++) {
                    StringBuilder buffer = new StringBuilder();
                    for (int j = 0; j < modelo.getColumnCount(); j++) {
                        buffer.append(modelo.getValueAt(i, j));
                        if (j != modelo.getColumnCount()) {
                            buffer.append(",");
                        }
                    }
                    writer.write(buffer.toString() + "\r\n");

                }
                writer.close();

            } catch (IOException e) {
                e.printStackTrace(System.out);
                JOptionPane pane = new JOptionPane("El sistema no tiene acceso al archivo: " + file.getName() + ",\nporques otro proceso lo tiene ocupado.\n Se requiere cerrar el archivo y volver a intentarlo.", JOptionPane.ERROR_MESSAGE, 0, icon2, new String[]{"Aceptar"}, "Aceptar");
                JDialog dialog = pane.createDialog(null, "Error al guardar el archivo de resultados");
                dialog.setIconImage((icon).getImage());
                dialog.setAlwaysOnTop(true);
                dialog.toFront();
                dialog.setVisible(true);
                return;
            } finally {

            }

            IJ.showStatus("El archivo: " + file.getName() + " se guardó");
            Interfazmac.statusBar.setText("El archivo: " + file.getName() + " se guardó OOO");
        }

    }

    public static void savePreferences(Properties prefs) {
        int n = Interfazmac.abrirrec.getItemCount();
        for (int i = 0; i < n; i++) {
            String key = "" + i;
            if (key.length() == 1) {
                key = "0" + key;
            }
            key = "recent" + key;
            prefs.put(key, Interfazmac.abrirrec.getItem(i).getName());
            prefs.put("lastdire", dir);
        }

    }

    /////cerrar todas la imagenes/////
    public static boolean closeAll() {
        int[] list = WindowManager.getIDList();
        if (list != null) {
            ij.Prefs.closingAll = true;
            for (int i = 0; i < list.length; i++) {
                ImagePlus imp = WindowManager.getImage(list[i]);
                if (imp != null) {
                    imp.changes = false;
                    imp.close();
                }
            }
            ij.Prefs.closingAll = false;
        }
        return true;
    }
    /////fin cerrar todas imagenes/////

    public static boolean closeAllroi() {
        RoiManager rm = RoiManager.getInstance();
        rm.setVisible(false);
        rm.close();
        return true;
    }

    private Color makeLut(int ubgcol) {
        byte[] reds = new byte[256];
        byte[] greens = new byte[256];
        byte[] blues = new byte[256];

        int cnt = 0;
        //System.out.println( "background " +Integer .toHexString(bgcol) +" ( "+ bgcol+ " )");
        Color c;
        //lut spectrum
        /*
	for (int i=0; i<256; i++) {
	c = Color.getHSBColor(i/255f, 255f, 255f);
	reds[i] = (byte)c.getRed();
	greens[i] = (byte)c.getGreen();
	blues[i] = (byte)c.getBlue();
	}
         */
        //lut ice
        //int[] r = {19,29,50,48,79,112,134,158,186,201,217,229,242,250,250,250,250,251,250,250,250,250,251,251,243,230};
        //int[] g = {193,184,171,162,146,125,107,93,81,87,92,97,95,93,93,90,85,69,64,54,47,35,19,0,4,0};
        //int[] b = {209,220,234,225,236,246,250,251,250,250,245,230,230,222,202,180,163,142,123,114,106,94,84,64,26,27};

        /*
int[] r = {255,0,255,0,0,255,0,255,0,154,0,120,31,255,177,241,254,221,32,114,118,2,200,136,255,133,161,20,0,220,147,0,0,57,238,0,171,161,164,255,71,212,251,171,117,166,0,165,98,0,0,86,159,66,255,0,252,159,167,74,0,145,207,195,253,66,106,181,132,96,255,102,254,228,17,210,91,32,180,226,0,93,166,97,98,126,0,255,7,180,148,204,55,0,150,39,206,150,180,110,147,199,115,15,172,182,216,87,216,0,243,216,1,52,255,87,198,255,123,120,162,105,198,121,0,231,217,255,209,36,87,211,203,62,0,112,209,0,105,255,233,191,69,171,14,0,118,255,94,238,159,80,189,0,88,71,1,99,2,139,171,141,85,150,0,255,222,107,30,173,255,0,138,111,225,255,229,114,111,134,99,105,200,209,198,79,174,170,199,255,146,102,111,92,172,210,199,255,250,49,254,254,68,201,199,68,147,22,8,116,104,64,164,207,118,83,0,43,160,176,29,122,214,160,106,153,192,125,149,213,22,166,109,86,255,255,255,202,67,234,191,38,85,121,254,139,141,0,63,255,17,154,149,126,58,189};
int[] g ={255,0,0,255,0,0,83,211,159,77,255,63,150,172,204,8,143,0,26,0,108,173,255,108,183,133,3,249,71,94,212,76,66,167,112,0,245,146,255,206,0,173,118,188,0,0,115,93,132,121,255,53,0,45,242,93,255,191,84,39,16,78,149,187,68,78,1,131,233,217,111,75,100,3,199,129,118,59,84,8,1,132,250,123,0,190,60,253,197,167,186,187,0,40,122,136,130,164,32,86,0,48,102,187,164,117,220,141,85,196,165,255,24,66,154,95,241,95,172,100,133,255,82,26,238,207,128,211,255,0,163,231,111,24,117,176,24,30,200,203,194,129,42,76,117,30,73,169,55,230,54,0,144,109,223,80,93,48,206,83,0,42,83,255,152,138,69,109,0,76,134,35,205,202,75,176,232,16,82,137,38,38,110,164,210,103,165,45,81,89,102,134,152,255,137,34,207,185,148,34,81,141,54,162,232,152,172,75,84,45,60,41,113,0,1,0,82,92,217,26,3,58,209,100,157,219,56,255,0,162,131,249,105,188,109,3,0,0,109,170,165,44,185,182,236,165,254,60,17,221,26,66,157,130,6,117};
int[] b ={255,255,0,0,51,182,0,0,255,66,190,193,152,253,113,92,66,255,1,85,149,36,0,0,159,103,0,255,158,147,255,255,80,106,254,100,204,255,115,113,21,197,111,0,215,154,254,174,2,168,131,0,63,66,187,67,124,186,19,108,166,109,0,255,64,32,0,84,147,0,211,63,0,127,174,139,124,106,255,210,20,68,255,201,122,58,183,0,226,57,138,160,49,1,129,38,180,196,128,180,185,61,255,253,100,250,254,113,34,103,105,182,219,54,0,1,79,133,240,49,204,220,100,64,70,69,233,209,141,3,193,201,79,0,223,88,0,107,197,255,137,46,145,194,61,25,127,200,217,138,33,148,128,126,96,103,159,60,148,37,255,135,148,0,123,203,200,230,68,138,161,60,0,157,253,77,57,255,101,48,80,32,0,255,86,77,166,101,175,172,78,184,255,159,178,98,147,30,141,78,97,100,23,84,240,0,58,28,121,0,255,38,215,155,35,88,232,87,146,229,36,159,207,105,160,113,207,89,34,223,204,69,97,78,81,248,73,35,18,173,0,51,2,158,212,89,193,43,40,246,146,84,238,72,101,101};
         */
        //Glasbey.lut
        int[] r = {255, 0, 255, 0, 0, 255, 0, 255, 0, 154, 0, 120, 31, 255, 177, 241, 254, 221, 32, 114, 118, 2, 200, 136, 255, 133,
            161, 20, 0, 220, 147, 0, 0, 57, 238, 0, 171, 161, 164, 255, 71, 212, 251, 171, 117, 166, 0, 165, 98, 0, 0, 86, 159, 66,
            255, 0, 252, 159, 167, 74, 0, 145, 207, 195, 253, 66, 106, 181, 132, 96, 255, 102, 254, 228, 17, 210, 91, 32, 180, 226,
            0, 93, 166, 97, 98, 126, 0, 255, 7, 180, 148, 204, 55, 0, 150, 39, 206, 150, 180, 110, 147, 199, 115, 15, 172, 182, 216,
            87, 216, 0, 243, 216, 1, 52, 255, 87, 198, 255, 123, 120, 162, 105, 198, 121, 0, 231, 217, 255, 209, 36, 87, 211, 203, 62,
            0, 112, 209, 0, 105, 255, 233, 191, 69, 171, 14, 0, 118, 255, 94, 238, 159, 80, 189, 0, 88, 71, 1, 99, 2, 139, 171, 141,
            85, 150, 0, 255, 222, 107, 30, 173, 255, 0, 138, 111, 225, 255, 229, 114, 111, 134, 99, 105, 200, 209, 198, 79, 174, 170,
            199, 255, 146, 102, 111, 92, 172, 210, 199, 255, 250, 49, 254, 254, 68, 201, 199, 68, 147, 22, 8, 116, 104, 64, 164, 207,
            118, 83, 0, 43, 160, 176, 29, 122, 214, 160, 106, 153, 192, 125, 149, 213, 22, 166, 109, 86, 255, 255, 255, 202, 67, 234,
            191, 38, 85, 121, 254, 139, 141, 0, 63, 255, 17, 154, 149, 126, 58};
        int[] g = {255, 0, 0, 255, 0, 0, 83, 211, 159, 77, 255, 63, 150, 172, 204, 8, 143, 0, 26, 0, 108, 173, 255, 108, 183, 133, 3,
            249, 71, 94, 212, 76, 66, 167, 112, 0, 245, 146, 255, 206, 0, 173, 118, 188, 0, 0, 115, 93, 132, 121, 255, 53, 0, 45, 242,
            93, 255, 191, 84, 39, 16, 78, 149, 187, 68, 78, 1, 131, 233, 217, 111, 75, 100, 3, 199, 129, 118, 59, 84, 8, 1, 132, 250,
            123, 0, 190, 60, 253, 197, 167, 186, 187, 0, 40, 122, 136, 130, 164, 32, 86, 0, 48, 102, 187, 164, 117, 220, 141, 85, 196,
            165, 255, 24, 66, 154, 95, 241, 95, 172, 100, 133, 255, 82, 26, 238, 207, 128, 211, 255, 0, 163, 231, 111, 24, 117, 176,
            24, 30, 200, 203, 194, 129, 42, 76, 117, 30, 73, 169, 55, 230, 54, 0, 144, 109, 223, 80, 93, 48, 206, 83, 0, 42, 83, 255,
            152, 138, 69, 109, 0, 76, 134, 35, 205, 202, 75, 176, 232, 16, 82, 137, 38, 38, 110, 164, 210, 103, 165, 45, 81, 89, 102,
            134, 152, 255, 137, 34, 207, 185, 148, 34, 81, 141, 54, 162, 232, 152, 172, 75, 84, 45, 60, 41, 113, 0, 1, 0, 82, 92, 217,
            26, 3, 58, 209, 100, 157, 219, 56, 255, 0, 162, 131, 249, 105, 188, 109, 3, 0, 0, 109, 170, 165, 44, 185, 182, 236, 165,
            254, 60, 17, 221, 26, 66, 157, 130, 6};
        int[] b = {255, 255, 0, 0, 51, 182, 0, 0, 255, 66, 190, 193, 152, 253, 113, 92, 66, 255, 1, 85, 149, 36, 0, 0, 159, 103, 0,
            255, 158, 147, 255, 255, 80, 106, 254, 100, 204, 255, 115, 113, 21, 197, 111, 0, 215, 154, 254, 174, 2, 168, 131, 0, 63,
            66, 187, 67, 124, 186, 19, 108, 166, 109, 0, 255, 64, 32, 0, 84, 147, 0, 211, 63, 0, 127, 174, 139, 124, 106, 255, 210,
            20, 68, 255, 201, 122, 58, 183, 0, 226, 57, 138, 160, 49, 1, 129, 38, 180, 196, 128, 180, 185, 61, 255, 253, 100, 250,
            254, 113, 34, 103, 105, 182, 219, 54, 0, 1, 79, 133, 240, 49, 204, 220, 100, 64, 70, 69, 233, 209, 141, 3, 193, 201, 79,
            0, 223, 88, 0, 107, 197, 255, 137, 46, 145, 194, 61, 25, 127, 200, 217, 138, 33, 148, 128, 126, 96, 103, 159, 60, 148,
            37, 255, 135, 148, 0, 123, 203, 200, 230, 68, 138, 161, 60, 0, 157, 253, 77, 57, 255, 101, 48, 80, 32, 0, 255, 86, 77,
            166, 101, 175, 172, 78, 184, 255, 159, 178, 98, 147, 30, 141, 78, 97, 100, 23, 84, 240, 0, 58, 28, 121, 0, 255, 38, 215,
            155, 35, 88, 232, 87, 146, 229, 36, 159, 207, 105, 160, 113, 207, 89, 34, 223, 204, 69, 97, 78, 81, 248, 73, 35, 18, 173,
            0, 51, 2, 158, 212, 89, 193, 43, 40, 246, 146, 84, 238, 72, 101};

        while (cnt < r.length) {
            reds[cnt] = (byte) r[cnt];
            greens[cnt] = (byte) g[cnt];
            blues[cnt] = (byte) b[cnt];
            cnt++;
        }
        IndexColorModel cm = new IndexColorModel(8, 256, reds, greens, blues);
        Color color = new Color(cm.getRed(ubgcol + 25), cm.getGreen(ubgcol + 25), cm.getGreen(ubgcol + 25), 150);
        //Color color = new Color(cm.getRGB(ubgcol+5));

        return color;
    }

    private static Color getMaskColor(int index) {
        Color color = Color.cyan;
        if (index < 0) {
            index = 0;
        }
        if (glasbeyLut == null) {
            ImageJ ij = IJ.getInstance();
            InputStream is = ij.getClass().getResourceAsStream("/macros/Glasbey.lut");
            //String path = IJ.getDir("/macros/Glasbey.lut");
            String path = ij.getClass().getResource("/macros/Glasbey.lut").getPath();
            //path.toString().replaceAll("%20", " ");
            path = path.replaceAll("\\%20", " ");
            glasbeyLut = LutLoader.openLut("noerror:" + path);
            if (glasbeyLut == null) {
                path = ij.getClass().getResource("/macros/Glasbey.lut").getPath();
                path.toString().replaceAll("%20", " ");
                glasbeyLut = LutLoader.openLut("noerror:" + path);
                System.out.println("La ruta es: " + path);
            }
            if (glasbeyLut == null) {
                IJ.log("LUT not found: " + path);
            }
        }
        if (glasbeyLut != null) //color = new Color(glasbeyLut.getRed(index+5), glasbeyLut.getGreen(index+5), glasbeyLut.getGreen(index+5)); // skip problematic white and black entries
        {
            color = new Color(glasbeyLut.getRGB(index + 5));
        }
        return color;
    }

    private static String getMessage(String message, int DialogAncho) {
        String string;
        JLabel label = new JLabel(message);
        if (label.getPreferredSize().width > DialogAncho) {
            string = "<html><body style=\'text-align: justify;\'> <p style='width:" + DialogAncho
                    + "px;'> <font face=\"Dialog\" size=\"4\" color=\"black\">" + message + "</p></body></html>";
        } else {
            string = "<html><body><p>" + message + "</p></body></html>";
        }
        return string;
    }

    public void showMessageBox(String TitleDialog, String message, int DialogAncho, String buttok, String buttocancel) {
        String stringmsj;
        String stringtitle;
        JLabel labeltitulo = new JLabel(TitleDialog);
        JLabel labelmsj = new JLabel(message);
        labeltitulo.setFont(new Font("Dialog", Font.PLAIN, 14));
        labelmsj.setFont(new Font("Dialog", Font.PLAIN, 14));
        stringtitle = "<html><body style=\'text-align: center;\'>" + TitleDialog + "</body></html>";
        String t = "<html><font color=#ffffdd>Hello</font> world!";
        stringmsj = "<html><body style=\'text-align: justify;\'> <p style='width:" + DialogAncho
                + "px;'> <font face=\"Dialog\" size=\"4\" color=\"black\">" + message + "</p></body></html>";
        //Redone for larger OK button
        JOptionPane theOptionPane;
        theOptionPane = new JOptionPane(stringmsj, JOptionPane.ERROR_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        JPanel buttonPanel = (JPanel) theOptionPane.getComponent(1);
        JButton buttonOk = (JButton) buttonPanel.getComponent(0);
        JButton buttonCancel = (JButton) buttonPanel.getComponent(1);
        buttonCancel.setText(buttocancel);
        buttonCancel.setFont(new Font("Dialog", Font.PLAIN, 14));
        buttonCancel.setPreferredSize(new Dimension(100, 25));
        buttonCancel.validate();

        if (buttocancel == null) {
            theOptionPane.setOptions(new JButton[]{buttonOk});
        } else {
            theOptionPane.setOptions(new JButton[]{buttonOk, buttonCancel});
        }
        buttonOk.setText(buttok);
        buttonOk.setFont(new Font("Dialog", Font.PLAIN, 14));
        buttonOk.setPreferredSize(new Dimension(100, 25));  //Set Button size here
        buttonOk.validate();
        JDialog theDialog = theOptionPane.createDialog(Interfazmac.myJFrame, TitleDialog);
        theDialog.setFont(new Font("Dialog", Font.PLAIN, 14));
        theDialog.setTitle(TitleDialog);
        theDialog.getLayeredPane().getComponent(0).setFont(new Font("Dialog", Font.PLAIN, 14));
        theDialog.setIconImage((icon).getImage());
        theDialog.setVisible(true);  //present your new optionpane to the world.

    }

    /*
    public ProgressBar getProgressBar() {
        return progressBar;
    }
     */
    /**
     * Returns the time in milliseconds when startTiming() was last called.
     */
    float calculateMean(float[] dataset) {
        double mValue = 0;
        for (int j = 0; j < n; j++) {
            mValue += dataset[j];
        }
        return (float) (mValue / n);
    }

    void showAbout() {
        IJ.showMessage("About MacgrainIJ...",
                "El análisis de imágenes.\n"
        );

    }

    private void appendText(String text, AttributeSet attributes) {
        try {
            Document doc = Interfazmac.textPane1.getDocument();
            doc.insertString(doc.getLength(), text, attributes);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Object o = evt.getNewValue();
        if (o instanceof FileNameExtensionFilter) {
            FileNameExtensionFilter filter = (FileNameExtensionFilter) o;

            String ex = filter.getExtensions()[0];

            File selectedFile = chooser.getSelectedFile();
            if (selectedFile == null) {
                selectedFile = csvFile;
            }
            String path = selectedFile.getName();
            path = path.substring(0, path.lastIndexOf("."));

            //xlsFile = path + "." + ex;
            String xlsFile = dir + "Resumen Conteo %d." + ex;
            csvFile = null;
            for (int i = 1;; i++) {
                csvFile = new File(String.format(xlsFile, i));
                if (!csvFile.exists()) {
                    filename = csvFile.getPath();
                    break;
                }
            }
            chooser.setSelectedFile(new File(filename));

        }

    }

}
