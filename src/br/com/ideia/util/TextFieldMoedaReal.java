package br.com.ideia.util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;

import javax.swing.JTextField;

/**
 * Classe que faz a validação de valores monetários em Real
 */
public class TextFieldMoedaReal extends JTextField implements KeyListener {
	
	private static final long serialVersionUID = 1L;
	private StringBuilder lastValidNumber;

	/**
	 * Aceita um BigDecimal com escala 2. Outras escalas não são serão aceitas
	 * @param BigDecimal
	 */
	public void setNumber(BigDecimal decimal) {
		if (decimal.scale() == 2) {
			lastValidNumber = new StringBuilder(decimal.unscaledValue().toString());
			super.setText(getRealFormat());
		}
	}

	/**
	 * Retorna um BigDecimal com o valor encontrando no campo e com escala igual a 2
	 * @return BigDecimal
	 */
	public BigDecimal getNumber() {		
		if (lastValidNumber.length() == 0) {
			return null;
		} else {
			return new BigDecimal(lastValidNumber.toString()).setScale(2).divide(new BigDecimal(100));
		}		
	}

	/**
	 * Aceita valor em centavos. O valor não deve possuir ponto ou vírgula
	 * @param String
	 */
	public void setText(String number) {
		if (isNumber(number)) {
			lastValidNumber = new StringBuilder(number);
		}
		super.setText(getRealFormat());
	}

	private boolean isNumber(String number) {
		for (char c : number.toCharArray()) {
			if (!Character.isDigit(c))
				return false;
		}
		return true;
	}

	public TextFieldMoedaReal() {
		super();
		this.setCaretPosition(this.getText().length());
		this.addKeyListener(this);
		lastValidNumber = new StringBuilder();
		super.setText(getRealFormat());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (Character.isDigit(e.getKeyChar())) {
			lastValidNumber.append(e.getKeyChar());
		} else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			if (lastValidNumber.length() > 0) {
				lastValidNumber.deleteCharAt(lastValidNumber.length() - 1);
			}
		}
		this.setText(getRealFormat());
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
		char key = e.getKeyChar();
		boolean press = (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_DELETE || Character.isDigit(key));
		if (!press) {
			e.consume();
		}
	}

	private String getRealFormat() {
		if (lastValidNumber.length() == 0) {
			return "0,00";
		} else if (lastValidNumber.length() == 1) {
			return "0,0" + lastValidNumber;
		} else if (lastValidNumber.length() == 2) {
			return "0," + lastValidNumber;
		} else {
			return buildPrefixSeparateWithDots() + lastValidNumber.substring(lastValidNumber.length() - 2);
		}
	}

	private String buildPrefixSeparateWithDots() {
		StringBuilder builder = new StringBuilder();
		for (int index = 0; index < lastValidNumber.length() - 2; ++index) {
			builder.append(lastValidNumber.charAt(index));
			if ((lastValidNumber.length() - index) % 3 == 0 && lastValidNumber.length() - index > 5) {
				builder.append(".");
			}
		}
		builder.append(',');
		return builder.toString();
	}
}