package banco.app;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

public class Programa extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField textField;

	/**
	 * Create the panel.
	 */
	public Programa() {
		
		JButton btnNewButton_1 = new JButton("Operações");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		textField = new JTextField();
		add(textField);
		textField.setColumns(10);
		add(btnNewButton_1);
		
		JButton btnNewButton = new JButton("Cadastrar Cliente");
		add(btnNewButton);

	}

}
