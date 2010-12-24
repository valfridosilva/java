package br.com.ideia.util;

import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
/**
 * Button que aceita a tecla ENTER como click
 * @author Valfrido
 *
 */
public class MyJButton extends JButton {

	private static final long serialVersionUID = 7024644780253018363L;

	public MyJButton() {
		super();
		addListenerKeyEnter();
	}

	public MyJButton(Action arg0) {
		super(arg0);
		addListenerKeyEnter();
	}

	public MyJButton(Icon arg0) {
		super(arg0);
		addListenerKeyEnter();
	}

	public MyJButton(String arg0, Icon arg1) {
		super(arg0, arg1);
		addListenerKeyEnter();
	}

	public MyJButton(String arg0) {
		super(arg0);
		addListenerKeyEnter();
		setToolTipText(arg0);
	}
	
	private void addListenerKeyEnter(){
		this.registerKeyboardAction(this.getActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false)), KeyStroke.getKeyStroke(
				KeyEvent.VK_ENTER, 0, false), JComponent.WHEN_FOCUSED);

		this.registerKeyboardAction(this.getActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true)), KeyStroke.getKeyStroke(
				KeyEvent.VK_ENTER, 0, true), JComponent.WHEN_FOCUSED);
	}

}
