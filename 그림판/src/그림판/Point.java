package 그림판;
import java.awt.Color;

public class Point implements Cloneable{
	public int x1;
	public int y1;
	public int x2;
	public int y2;
	public int option;
	public Color selectedColor;
//	public Color beforeColor;
	public int stroke;
	public boolean selected=false;
	public boolean filled= false;
	
	public Point() {
		
	}
	
	public Point(int x1, int y1, int x2, int y2, int option) {
		this.x1=x1;
		this.y1=y1;
		this.x2=x2;
		this.y2=y2;
		this.option=option;
	}
	
	public void setPoint(int x1, int y1, int x2, int y2, int option) {
		this.x1=x1;
		this.y1=y1;
		this.x2=x2;
		this.y2=y2;
		this.option=option;
	}
	public void setPoint(int x1, int y1, int x2, int y2, int option, Color selectedColor) {
		setPoint(x1, y1, x2, y2, option);
		this.selectedColor=selectedColor;
	}

	
	public void setStroke(int stroke) {
		this.stroke=stroke;
	}
	public void setOption(int option) {
		this.option=option;
	}

	public void printPoint() {
		System.out.println("point 1:"+x1+","+y1+"/ point 2: "+x2+","+y2+"/"+option+"/selected:"+selected+"/filled:"+filled);
	}
	
	public Object clone() {
		Object newPoint = null;
		try {
			newPoint=super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newPoint;
	}
	
}
