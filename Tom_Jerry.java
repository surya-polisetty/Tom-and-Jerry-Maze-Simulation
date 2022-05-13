import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.LinkedList;
import java.util.Queue;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.image.BufferedImage;

public class Tom_Jerry extends JFrame {
	BufferedImage image;

	public static void main(String[] args) {
		Tom_Jerry tj = new Tom_Jerry();
		tj.setVisible(true);
	}

	private Tom_Jerry() {
		setBounds(250, 200, 710, 340);
		// setSize(700, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setTitle("Tom&Jerry");

		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
			//banner
			JPanel banner = new JPanel() {
				private static final long serialVersionUID = 1L;

				@Override
				public void paintComponent(Graphics g) {
					try {
						image = ImageIO.read(this.getClass().getResource("banner.jpg"));
					} catch (IOException ex) {
						System.out.println("not found");
					}

					g.drawImage(image, 0, 0, null);
				}
			};
			banner.setMinimumSize(new Dimension(400, 320));

			//rowsNcols
			JPanel rc = new JPanel();
			rc.setMaximumSize(new Dimension(300,300));
			rc.setMinimumSize(new Dimension(200,300));
			rc.setLayout(null);

				JLabel lblRow = new JLabel("rows (0-100)");
				lblRow.setFont(new Font("Tahoma", Font.BOLD, 16));
				lblRow.setBounds(25, 40, 110, 35);
				
				JLabel lblColumn = new JLabel("cols (0-100)");
				lblColumn.setFont(new Font("Tahoma", Font.BOLD, 16));
				lblColumn.setBounds(25, 100, 110, 19);
				
				JTextField textField = new JTextField();
				textField.setBounds(140, 46, 116, 22);
				textField.setColumns(10);
				
				JTextField textField_1 = new JTextField();
				textField_1.setBounds(140, 97, 116, 22);
				textField_1.setColumns(10);
			
			
				JButton btnClickMe = new JButton("GO");
				btnClickMe.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						try {
							int a = Integer.parseInt(textField.getText());
							int b = Integer.parseInt(textField_1.getText());
							new Grid(a, b);
						} catch (Exception e) {
							JOptionPane.showMessageDialog(null, "Enter  valid rows and columns");
						}
					}
				});
				btnClickMe.setBounds(73, 156, 97, 25);

			rc.add(lblRow);
			rc.add(lblColumn);
			rc.add(textField);
			rc.add(textField_1);
			rc.add(btnClickMe);

		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, banner, rc);
		cp.add(split);
	}
}

class Grid implements ActionListener {

	private int a;
	private int b;
	private int prevButton = 0, xs=0, xd=0, ys=0, yd=0;
	JFrame frame = new JFrame();
	JButton[][] cells = new JButton[100][100];
	int[][] Arr = new int[100][100];
	final Color color = UIManager.getColor("Panel.background");
	
	Grid(int a, int b) {
		this.a = a;
		this.b = b;
		frame.setTitle("Tom&Jerry");
		frame.setVisible(true);
		frame.setBounds(0, 0, 125, 600);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

			JPanel maze = new JPanel();
			maze.setLayout(new GridLayout(a, b));
			maze.setBackground(color);
			for (int i = 0; i < a; ++i)	for (int j = 0; j < b; ++j) {
				cells[i][j] = new JButton();
				cells[i][j].setBackground(color);
				cells[i][j].setForeground(color);
				cells[i][j].setText(i+"-"+j);
				//
				cells[i][j].addActionListener(this);
				maze.add(cells[i][j]);
			}
			
			JPanel bottom = new JPanel();
			bottom.setBackground(color);

				JButton source = new JButton("Place Tom");
				JButton dest = new JButton("Place Jerry");
				JButton ran = new JButton("Let Jerry Keep Obstacles");
				JButton obs = new JButton("Modify Obstacles");
				JButton run = new JButton("CATCH");
				JButton reset = new JButton("Reset");
				source.addActionListener(new tomHandler());
				dest.addActionListener(new jerryHandler());
				obs.addActionListener(new obstacleHandler());
				run.addActionListener(new catchHandler());
				reset.addActionListener(new resetHandler());
				ran.addActionListener(new randomObstaclesHandler());
		
			bottom.add(source);
			bottom.add(dest);
			bottom.add(ran);
			bottom.add(run);
			bottom.add(obs);
			bottom.add(reset);

		frame.add(maze);
		frame.add(bottom, BorderLayout.SOUTH);
		frame.pack();
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton cell = (JButton) e.getSource();
		int[] cord = coordinatesIJ(cell.getText());

		if (prevButton == 1 && cell.getBackground()==color) {
			
			cells[xs][ys].setBackground(color);
			cells[xs][ys].setForeground(color);

			cell.setBackground(Color.BLUE);
			cell.setForeground(Color.BLUE);
			xs = cord[0];
			ys = cord[1];
			Arr[xs][ys] = 0;

		}

		if (prevButton == 2 && cell.getBackground()==color) {

			cells[xd][yd].setBackground(color);
			cells[xd][yd].setForeground(color);

			cell.setBackground(Color.ORANGE);
			cell.setForeground(Color.ORANGE);
			xd = cord[0];
			yd = cord[1];
			Arr[xd][yd] = 0;

		}

		if (prevButton == 3) {
			if (cell.getBackground()==color) {
				cell.setBackground(Color.DARK_GRAY);
				cell.setForeground(Color.DARK_GRAY);
				Arr[cord[0]][cord[1]] = -1;
			}
			else if (cell.getBackground()==Color.DARK_GRAY||cell.getBackground()==Color.CYAN) {
				cell.setBackground(color);
				cell.setForeground(color);
				Arr[cord[0]][cord[1]] = 0;
			}
		}

	}

	class tomHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			prevButton=1;
		}
	}

	class jerryHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			prevButton=2;
		}
	}

	class obstacleHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			prevButton=3;
		}
	}

	class resetHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			for (int i = 0; i < a; ++i) for (int j = 0; j < b; ++j) {
				cells[i][j].setBackground(color);
				cells[i][j].setForeground(color);
				Arr[i][j] = 0;
			}
			prevButton = 0;
		}
	}

	class randomObstaclesHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			int loop = ((int) (Math.random() * 1000) % ((a * b) / 5));
			while (loop > 0) {
				loop--;
				int ll = ((int) (Math.random() * 1000)) % a;
				int rr = ((int) (Math.random() * 1000)) % b;
				if ( !((ll == xs && rr == ys)||(ll == xd && rr == yd)) ) {
					cells[ll][rr].setBackground(Color.DARK_GRAY);
					cells[ll][rr].setForeground(Color.DARK_GRAY);
					Arr[ll][rr] = -1;
				}
			}
		}
	}

	class catchHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {

			// Cleaning previous path and cleaning Arr from previous CATCH except for obstacles
			for(int i=0;i<a;i++) for(int j=0;j<b;j++) {
				if(Arr[i][j]!=-1){
					Arr[i][j]=0;
					if(cells[i][j].getBackground()==Color.CYAN){
						cells[i][j].setBackground(color);
						cells[i][j].setForeground(color);
					}
				}
			}

			buildPath();

			int i = xd;
			int j = yd;
			if (Arr[xd][yd] == 0) {
				JOptionPane.showMessageDialog(null, "Oops!\nTOM can't catch JERRY");
			}
			else {
				int count = Arr[i][j];
				while (count != 0) {
					count--;
					int ss;
					if (((i + 1) < a && cells[i + 1][j].getText() != "x")) {
						ss = Arr[i + 1][j];
						if (count == ss) {
							cells[i + 1][j].setBackground(Color.CYAN);
							cells[i + 1][j].setForeground(Color.CYAN);
							i++;
							continue;
						}

					}
					if ((i - 1) >= 0 && cells[i - 1][j].getText() != "x") {
						ss = Arr[i - 1][j];
						if (count == ss) {
							cells[i - 1][j].setBackground(Color.CYAN);
							cells[i - 1][j].setForeground(Color.CYAN);
							i--;
							continue;
						}
					}
					if ((j + 1) < b && cells[i][j + 1].getText() != "x") {
						ss = Arr[i][j + 1];
						if (count == ss) {
							cells[i][j + 1].setBackground(Color.CYAN);
							cells[i][j + 1].setForeground(Color.CYAN);

							j++;
							continue;
						}
					}

					if ((j - 1) >= 0 && cells[i][j - 1].getText() != "x") {
						ss = Arr[i][j - 1];
						if (count == ss) {
							cells[i][j - 1].setBackground(Color.CYAN);
							cells[i][j - 1].setForeground(Color.CYAN);
							j--;
							continue;
						}
					}

				}
				cells[xs][ys].setBackground(Color.LIGHT_GRAY);
				cells[xs][ys].setForeground(Color.LIGHT_GRAY);
				cells[xd][yd].setBackground(Color.BLUE);
				cells[xd][yd].setForeground(Color.BLUE);

				JOptionPane.showMessageDialog(null, "Success!\nTOM caught JERRY");
			}

		}
	}

	void buildPath(){
		
		int i, j;
		Queue<Integer> q1 = new LinkedList<Integer>();
		Queue<Integer> q2 = new LinkedList<Integer>();
		q1.add(xs);
		q2.add(ys);
		while (!q1.isEmpty() && !q2.isEmpty()) {
			i = q1.poll();
			j = q2.poll();
			if (((i + 1) < a)) {
				if (Arr[i + 1][j] == 0 && !((i + 1) == xs && j == ys)) {
					Arr[i + 1][j] = Arr[i][j] + 1;
					q1.add(i + 1);
					q2.add(j);
				}
			}

			if ((i - 1) >= 0) {
				if (Arr[i - 1][j] == 0 && !((i - 1) == xs && j == ys)) {
					Arr[i - 1][j] = Arr[i][j] + 1;
					q1.add(i - 1);
					q2.add(j);
				}
			}
			if ((j + 1) < b) {
				if (Arr[i][j + 1] == 0 && !((i) == xs && (j + 1) == ys)) {
					Arr[i][j + 1] = Arr[i][j] + 1;
					q1.add(i);
					q2.add(j + 1);
				}
			}

			if ((j - 1) >= 0) {
				if (Arr[i][j - 1] == 0 && !((i) == xs && (j - 1) == ys)) {
					Arr[i][j - 1] = Arr[i][j] + 1;
					q1.add(i);
					q2.add(j - 1);
				}
			}
		}

	}

	int[] coordinatesIJ(String s){
		int[] cord = new int[2];
		String[] arr = s.split("-",-1);
		cord[0] = Integer.parseInt(arr[0]);
		cord[1] = Integer.parseInt(arr[1]);
		return cord;
	}
}