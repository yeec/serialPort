package bros.manage.business.view;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

public class LocaleCellRenderer extends DefaultListCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3007170855930794730L;

	@SuppressWarnings("rawtypes")
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		super.getListCellRendererComponent(list, value, index, isSelected,cellHasFocus);
		JLabel jl = (JLabel) value;
		setText(jl.getText());
		setBackground(jl.getBackground());
		setForeground(jl.getForeground());
		return this;
	}



}
