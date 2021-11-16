package 그림판;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Vector;

import javax.swing.JPanel;
import javax.accessibility.Accessible;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.html.ImageView;
import javax.swing.event.ChangeEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;


public class MyDrawing extends JPanel implements MouseListener, MouseMotionListener, ActionListener, Runnable{
	private int x1, x2, y1, y2; //그림그리기용
	private int x, y; // select 파악하는 용
	public JPanel contentPane;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private String button;
	private int option;
	private int stroke;
	private int beforeOption;
	private boolean canMove;
	private Color selectedColor=Color.black;
	private Color backgroundColor;
	private Point selectedShape;
	private Point copyShape;
	private int distX1;
	private int distY1;
	private int distX2;
	private int distY2;
	private boolean background=false; //바탕색 칠해야되는 경우에만
	private int globalI;
	JColorChooser chooser=new JColorChooser();
	
	Vector<Point> beforeList = new Vector<>();
	Vector<Point> groupingList = new Vector<>();
	Vector<Point> sayGoodBye = new Vector<>();

	
	public static final int DEFAULT = 0;
	public static final int PEN= 1;
	public static final int CIRCLE = 2;
	public static final int RECT = 3;
	public static final int LINE = 4;
	public static final int ERASER = 5;
	public static final int CLEAR = 6;
	public static final int MOVE = 7;
	public static final int FILL = 8;
	public static final int EXIT = 9;
	
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setTitle("그림판");
		frame.setSize(1200,1000);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(0, 0, 1200, 1000);
		
		MyDrawing drawingPanel = new MyDrawing();
		Container contentPane = frame.getContentPane();
		drawingPanel.setBackground(Color.white);
//		drawingPanel.addKeyListener(drawingPanel);
//		frame.addKeyListener(drawingPanel);
		contentPane.add(drawingPanel);
//		contentPane.addKeyListener(drawingPanel);
		contentPane.add(drawingPanel.contentPane);
		frame.setVisible(true);
		
	}

	/*
	 * 
	 * 캔버스------------------------------------------------------------------
	 * 
	 */	

	public void beforeDraw(Graphics2D g) {

		
		if(background==true) {
//			System.out.println("배경색을 칠할꺼야~~");
			g.setColor(backgroundColor);
//			g.setColor(Color.red);
			g.fillRect(0,0,1200,1000);
		}
		
//		System.out.println(beforeList.size());
		for(int i=0;i<beforeList.size();i++) {
			Point p=(Point)beforeList.get(i);
//			p.printPoint();
			int minX = (int)Math.min(p.x1, p.x2);
			int maxX=(int)Math.max(p.x1,p.x2);
			int minY = (int)Math.min(p.y1, p.y2);
			int maxY=(int)Math.max(p.y1,p.y2);
			int width = (int)Math.abs(p.x1- p.x2);
			int height = (int)Math.abs(p.y1- p.y2);
			
			
			if(option==MOVE&&p.selected==true) g.setColor(Color.yellow); //선택될때는 노랑
			else g.setColor(p.selectedColor); //아니면 그냥 원래 지정된 색깔
			
//											System.out.println(p.selectedColor+"is selected Color");
			g.setStroke(new BasicStroke(p.stroke));
			
			
			switch(p.option) {
				case PEN:
					g.drawLine(p.x1, p.y1, p.x2, p.y2 );
					break;
				case CIRCLE:
					if(p.filled==true) g.fillOval(minX-p.stroke/2, minY-p.stroke/2, width+p.stroke, height+p.stroke);//크기보정 필요
					else g.drawOval(minX, minY, width, height);
					break;
				case RECT:
					if(p.filled==true) g.fillRect(minX-p.stroke/2, minY-p.stroke/2, width+p.stroke, height+p.stroke);//크기보정 필요
					g.drawRect(minX, minY, width, height);
					break;
				case LINE:
//					System.out.println("line");
					g.drawLine(p.x1, p.y1, p.x2, p.y2);
//					p.printPoint();
					break;
				case ERASER:
					g.setColor(Color.white);
					g.drawLine(p.x1, p.y1, p.x2, p.y2) ;
					break;
				case CLEAR:
//					System.out.println("before clear");
					g.clearRect(0,0,1200,1000);
					break;
					
			}
		}
	}
	
	
	public void paintComponent (Graphics g0) {
			
			
		Graphics2D g= (Graphics2D)g0;
		super.paintComponent(g);

		
		beforeDraw(g);
		
		g.setColor(selectedColor);
		g.setStroke(new BasicStroke(stroke));
		
		
		int minX = (int)Math.min(x1, x2);
		int maxX=(int)Math.max(x1,x2);
		int minY = (int)Math.min(y1, y2);
		int maxY=(int)Math.max(y1,y2);
		int width = (int)Math.abs(x1- x2);
		int height = (int)Math.abs(y1- y2);
		

//		System.out.println(option);
		
		
		switch(option) {
			case PEN:
//				x1=x2;
//				y1=y2;
				g.drawLine(x1, y1, x2, y2 );
				break;
			case CIRCLE:
//				System.out.println("now circle");
				g.drawOval(minX, minY, width, height);
				break;
			case RECT:
//				System.out.println("now rect");
				g.drawRect(minX, minY, width, height);
				break;
			case LINE:
//				System.out.println("now line");
				g.drawLine(x1, y1,x2, y2);
				break;
			case ERASER:
				g.setColor(Color.white);
//				System.out.println("C");
				g.drawLine(x1, y1,x2, y2) ;
				break;
			case CLEAR:
//				System.out.println("clear");
				g.clearRect(0,0,1200,1000);
				option=beforeOption;
				break;
			case EXIT:

				makingGoodBye();
				drawEnding(g);
		}
	
	}
	
	
	public void drawEnding(Graphics2D g){
		
		g.clearRect(0,0,1200,1000);
		
		System.out.println(sayGoodBye.size());
		
		for(int i=0;i<sayGoodBye.size();i++) {
			Point p=(Point)sayGoodBye.get(i);
//			p.printPoint();
			int minX = (int)Math.min(p.x1, p.x2);
			int maxX=(int)Math.max(p.x1,p.x2);
			int minY = (int)Math.min(p.y1, p.y2);
			int maxY=(int)Math.max(p.y1,p.y2);
			int width = (int)Math.abs(p.x1- p.x2);
			int height = (int)Math.abs(p.y1- p.y2);
			
			
			if(option==MOVE&&p.selected==true) g.setColor(Color.yellow); //선택될때는 노랑
			else g.setColor(p.selectedColor); //아니면 그냥 원래 지정된 색깔
			
//											System.out.println(p.selectedColor+"is selected Color");
			g.setStroke(new BasicStroke(15));
			
			
			switch(p.option) {
				case PEN:
					g.drawLine(p.x1, p.y1, p.x2, p.y2 );
					break;
				case CIRCLE:
					if(p.filled==true) g.fillOval(minX-p.stroke/2, minY-p.stroke/2, width+p.stroke, height+p.stroke);//크기보정 필요
					else g.drawOval(minX, minY, width, height);
					break;
				case RECT:
					if(p.filled==true) g.fillRect(minX-p.stroke/2, minY-p.stroke/2, width+p.stroke, height+p.stroke);//크기보정 필요
					g.drawRect(minX, minY, width, height);
					break;
				case LINE:
//					System.out.println("line");
					g.drawLine(p.x1, p.y1, p.x2, p.y2);
//					p.printPoint();
					break;
				case ERASER:
					g.setColor(Color.white);
					g.drawLine(p.x1, p.y1, p.x2, p.y2) ;
					break;
				case CLEAR:
//					System.out.println("before clear");
					g.clearRect(0,0,1200,1000);
					break;
					
			}
		}
		
		
	}
	
	
	
//	public void groupingShape(int x, int y) {
//		Point p= new Point();
//
//		for(int i=beforeList.size()-1;i>=0;i--) { //최신순으로 도형 읽기
//			p=beforeList.get(i);
////			p.printPoint();
//			if(Math.min(p.x1, p.x2)<x&&x<Math.max(p.x1, p.x2)&&Math.min(p.y1,p.y2)<y&&y<Math.max(p.y1,p.y2)) {
////				System.out.println("found");
//					p.selected=true;	
//			}
//		}
//		groupingList.add(p);
//	}
//	
//	

	public Point findShape(int x, int y) {
		Point p= new Point();
		
		for(int i=0;i<beforeList.size();i++) {
			p=beforeList.get(i);
			p.selected=false;
		
		}
		
		
		for(int i=beforeList.size()-1;i>=0;i--) { //최신순으로 도형 읽기
			p=beforeList.get(i);
//			p.printPoint();
			if(Math.min(p.x1, p.x2)<x&&x<Math.max(p.x1, p.x2)&&Math.min(p.y1,p.y2)<y&&y<Math.max(p.y1,p.y2)&&p.option!=PEN&&p.option!=ERASER) {
//				System.out.println("found");
				p.selected=true;
				if(option==FILL) {
					if(p.filled) {
						p.filled=false;
						if(p.selectedColor!=selectedColor) p.filled=true; //다른색인경우엔 채우기모드 그대로 유지
					}
					else p.filled=true;
				}
				return p;
			}
		}
		if(option==FILL) {
			if(background==false) background=true; //아무것도 못찾았을때는 배경색을 바꾸겠다 !
			else  background=false; 
		}
		return p; 
	}
	
	/*
	 * 
	 * 마우스 핸들러 ------------------------------------------------------------------
	 * 
	 */
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(option==FILL) {
			x=e.getX();
			y=e.getY();
			copyShape=findShape(x,y);
			if(copyShape.selected==true){
				
				copyShape.selectedColor=selectedColor; //뭔가 이쪽에서 오류있음.
			}else {
				backgroundColor=selectedColor;
//				System.out.println("도형을 못찾았다. ");
			}
			this.repaint();

		}
	}
	
	
	@Override
	public void mousePressed(MouseEvent e) {
		switch(option) {
		case PEN:
		case CIRCLE:
		case RECT:
		case LINE:
		case ERASER:
			x1=e.getX();
			y1=e.getY();
			break;
		case MOVE:
			x=e.getX();
			y=e.getY();
			selectedShape=findShape(x,y);
			
			if(selectedShape.selected==true) {
				canMove=true;
				distX1=x-selectedShape.x1;
				distY1=y-selectedShape.y1;
				distX2=x-selectedShape.x2;
				distY2=y-selectedShape.y2;
//				System.out.println(selectedShape.x1);
//				System.out.println(x);
//				System.out.println(distX1 +"," +distY1);
			}
			this.repaint();
			break;

		default:
			break;
		}
		
		
	}


	@Override
	public void mouseDragged(MouseEvent e) {
		
		x2=e.getX();
		y2=e.getY();
		
		switch(option) {
		case PEN:
		case ERASER:

			Point tmp=new Point();
			tmp.setPoint(x1, y1, x2, y2, option,selectedColor);
			tmp.setStroke(stroke);
			beforeList.add(tmp);
			this.repaint();
			x1=x2;
			y1=y2;
			
			break;
		case CIRCLE:
		case RECT:
		case LINE:
			this.repaint();
			break;
		case MOVE:
			if(canMove==true) {
	
				x=getX();
				y=getY();
				selectedShape.x1=x2-distX1;
				selectedShape.y1=y2-distY1;
				selectedShape.x2=x2-distX2;
				selectedShape.y2=y2-distY2;
//				selectedShape.printPoint();
				this.repaint();
			}
			break;
		default:
			break;
		}

	}
	
	
	@Override
	public void mouseReleased(MouseEvent e) {
		
		switch(option) {
		case PEN:
		case CIRCLE:
		case RECT:
		case LINE:
		case ERASER:
			x2=e.getX();
			y2=e.getY();

			Point tmp=new Point();
			tmp.setPoint(x1, y1, x2, y2, option, selectedColor);
			tmp.setStroke(stroke);
			beforeList.add(tmp);
			
			this.repaint();
			break;
		case MOVE:
			canMove=false;
			selectedShape.selected=false;
			break;
		default:
			break;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseMoved(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}

	
	/*
	 * 
	 * 버튼 핸들러--------------------------------------------------------------------
	 * 
	 */
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		button=e.getActionCommand();
		switch(button) {
			case "Pen":
				option=PEN;
				beforeOption=PEN;
				break;
			case "Rectangle":
				option=RECT;
				beforeOption=RECT;
	
				break;
			case "Circle":
				option=CIRCLE;
				beforeOption=CIRCLE;
	
				break;
			case "Line":
				option=LINE;
				beforeOption=LINE;
				break;
			case "Color":
	            selectedColor=chooser.showDialog(null,"Color",Color.BLACK);
	            break;
			case "": 
				if(option!=ERASER)
					option=ERASER;
				else {
					option=beforeOption;
				}
				break;
			case "Clear": 
				option=CLEAR;
				this.repaint();
				
				Point tmp = new Point();
				tmp.setOption(option);
				beforeList.add(tmp);
				
				break;
			case "Move":
	            option=MOVE;
				beforeOption=MOVE;
	            break;

			case "Copy":
				Point copied= new Point();
				copied=(Point)selectedShape.clone();
				copied.x1+=20;
				copied.y1+=20;
				copied.x2+=20;
				copied.y2+=20;
				beforeList.add(copied);
				repaint();
				break;
			case "Fill":
				option=FILL;
				beforeOption=FILL;
				break;
			case "Exit":
				repaint();
				MyDrawing m=new MyDrawing();
				Thread thread1=new Thread(m);
				option=EXIT;
				thread1.start();
				break;

			
		}
	}
	/*
	 * 
	 * 쓰레드
	 * 
	 */
	
	public void run() {
		synchronized(this){
			makingGoodBye();
			
			try {
				Thread.sleep(2000);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
		
				System.exit(-1);
			
		}
		
	}
	
	
	/**
	 * 
	 * 프레임----------------------------------------------------------------------
	 * 
	 */
	
	public MyDrawing() {

		setBounds(110, 0, 1200, 1000);
		contentPane = new JPanel();
		contentPane.setLayout(null);
		addMouseListener(this);
		addMouseMotionListener(this);
//		addKeyListener(this);
//		contentPane.addKeyListener(this);
		
		JPanel toolPanel = new JPanel();
		toolPanel.setBackground(SystemColor.inactiveCaptionText);
		toolPanel.setBounds(0, 17, 110, 983);
		contentPane.add(toolPanel);
		toolPanel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Draw");
		lblNewLabel.setBounds(6, 19, 61, 16);
		toolPanel.add(lblNewLabel);
		
		JRadioButton penBtn = new JRadioButton("Pen");
		buttonGroup.add(penBtn);
		penBtn.addActionListener(this);
		penBtn.setBounds(0, 36, 99, 23);
		toolPanel.add(penBtn);
		
		
		JRadioButton rectBtn = new JRadioButton("Rectangle");
		rectBtn.addActionListener(this);
		buttonGroup.add(rectBtn);
		rectBtn.setBounds(0, 83, 99, 23);
		toolPanel.add(rectBtn);
		
		JRadioButton circleBtn = new JRadioButton("Circle");
		buttonGroup.add(circleBtn);
		circleBtn.addActionListener(this);
		circleBtn.setBounds(0, 59, 99, 23);
		toolPanel.add(circleBtn);
		
		JRadioButton lineBtn = new JRadioButton("Line");
		buttonGroup.add(lineBtn);
		lineBtn.addActionListener(this);
		lineBtn.setBounds(0, 107, 99, 23);
		toolPanel.add(lineBtn);
		
		
		JButton colorButton = new JButton("Color");
		colorButton.addActionListener(this);
		colorButton.setBounds(6, 264, 98, 29);
		toolPanel.add(colorButton);
		
		
		JSlider slider = new JSlider();
		slider.setMajorTickSpacing(14);
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider js = (JSlider) e.getSource();
				stroke=js.getValue();
			}
		});
		slider.setValue(10);
		slider.setPaintLabels(true);
		slider.setPaintTicks(true);
		slider.setMinimum(1);
		slider.setMaximum(50);
		slider.setBackground(SystemColor.activeCaption);
		slider.setBounds(0, 227, 110, 37);
		toolPanel.add(slider);
		
		JLabel lblThickness = new JLabel("\nThickness");
		lblThickness.setBounds(0, 214, 104, 16);
		toolPanel.add(lblThickness);
		
		
		
		JButton eraseButton = new JButton("");
		eraseButton.setIcon(new ImageIcon("./image/eraser.png"));
		buttonGroup.add(eraseButton);
		eraseButton.setBounds(6, 399, 98, 42);
		toolPanel.add(eraseButton);
		eraseButton.addActionListener(this);
		
		
		JButton ClearButton = new JButton("Clear");
		buttonGroup.add(ClearButton);
		ClearButton.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		ClearButton.setBounds(6, 441, 98, 42);
		toolPanel.add(ClearButton);
		ClearButton.addActionListener(this);
		
	
		
		
		
		

		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setForeground(SystemColor.inactiveCaptionBorder);
		menuBar.setBackground(SystemColor.activeCaptionBorder);
		menuBar.setBounds(0, 0, 1200, 18);
		contentPane.add(menuBar);
		
		JMenu fileMenu = new JMenu("File");
		fileMenu.setBackground(SystemColor.window);
		menuBar.add(fileMenu);
		
		JMenuItem saveItem = new JMenuItem("Save");
		fileMenu.add(saveItem);
		
		JMenuItem exitItem = new JMenuItem("Exit");
		fileMenu.add(exitItem);
		exitItem.addActionListener(this);

		
		
		JMenu editMenu = new JMenu("Edit");
		editMenu.setBackground(SystemColor.window);
		menuBar.add(editMenu);
		
		JMenuItem undoItem = new JMenuItem("Undo");
		editMenu.add(undoItem);
		
		JMenuItem redoItem = new JMenuItem("Redo");
		editMenu.add(redoItem);
		
		JColorChooser chooser=new JColorChooser();
		toolPanel.add(chooser);
		
		JLabel eraserLabel = new JLabel("Erase");
		eraserLabel.setBounds(6, 385, 104, 16);
		toolPanel.add(eraserLabel);
		
		
		
		
		
		
		
		
		
		
		JLabel selectLabel = new JLabel("select");
		selectLabel.setBounds(6, 579, 61, 16);
		toolPanel.add(selectLabel);
		
		JRadioButton moveBtn = new JRadioButton("Move");
		buttonGroup.add(moveBtn);
		moveBtn.setBounds(0, 597, 98, 23);
		toolPanel.add(moveBtn);
		moveBtn.addActionListener(this);
		
		JButton copyBtn = new JButton("Copy");
		copyBtn.setBounds(6, 653, 76, 29);
		toolPanel.add(copyBtn);
		copyBtn.addActionListener(this);
		
		JRadioButton fillBtn = new JRadioButton("Fill");
		fillBtn.setBounds(0, 618, 99, 23);
		buttonGroup.add(fillBtn);
		toolPanel.add(fillBtn);
		fillBtn.addActionListener(this);


	}
	
	public void helpingMaking() {
		for(int i=0;i<beforeList.size();i++) {
			Point p=(Point)beforeList.get(i);
	
			System.out.println("tmp = new Point(" +p.x1+","+p.y1+","+p.x2+","+p.y2+","+p.option+"); \n sayGoodBye.add(tmp); ");
		}
	}
	
	public void makingGoodBye() {
		Point tmp;
		tmp = new Point(87,123,159,215,3); 
		 sayGoodBye.add(tmp); 
		tmp = new Point(201,118,202,218,4); 
		 sayGoodBye.add(tmp); 
		tmp = new Point(202,170,232,173,4); 
		 sayGoodBye.add(tmp); 
		tmp = new Point(104,236,105,279,4); 
		 sayGoodBye.add(tmp); 
		tmp = new Point(105,279,226,277,4); 
		 sayGoodBye.add(tmp); 
		tmp = new Point(288,117,369,116,4); 
		 sayGoodBye.add(tmp); 
		tmp = new Point(283,121,281,188,4); 
		 sayGoodBye.add(tmp); 
		tmp = new Point(281,188,368,185,4); 
		 sayGoodBye.add(tmp); 
		tmp = new Point(266,207,391,208,4); 
		 sayGoodBye.add(tmp); 
		tmp = new Point(275,227,277,277,4); 
		 sayGoodBye.add(tmp); 
		tmp = new Point(278,278,378,277,4); 
		 sayGoodBye.add(tmp); 
		tmp = new Point(450,133,536,256,2); 
		 sayGoodBye.add(tmp); 
		tmp = new Point(576,115,577,281,4); 
		 sayGoodBye.add(tmp); 
		tmp = new Point(660,125,660,146,4); 
		 sayGoodBye.add(tmp); 
		tmp = new Point(662,261,662,282,4); 
		 sayGoodBye.add(tmp); 
		tmp = new Point(337,435,333,552,4); 
		 sayGoodBye.add(tmp); 
		tmp = new Point(333,552,442,553,4); 
		 sayGoodBye.add(tmp); 
		tmp = new Point(442,553,447,428,4); 
		 sayGoodBye.add(tmp); 
		tmp = new Point(338,486,440,490,4); 
		 sayGoodBye.add(tmp); 
		tmp = new Point(377,569,376,637,4); 
		 sayGoodBye.add(tmp); 
		tmp = new Point(254,641,489,642,4); 
		 sayGoodBye.add(tmp); 
		tmp = new Point(519,424,614,523,2); 
		 sayGoodBye.add(tmp); 
		tmp = new Point(623,453,683,449,4); 
		 sayGoodBye.add(tmp); 
		tmp = new Point(618,496,681,496,4); 
		 sayGoodBye.add(tmp); 
		tmp = new Point(688,410,685,559,4); 
		 sayGoodBye.add(tmp); 
		tmp = new Point(586,573,699,667,2); 
		 sayGoodBye.add(tmp); 
		 
//		 System.out.println(sayGoodBye.size());
		
		
		
	}
	
	
}
