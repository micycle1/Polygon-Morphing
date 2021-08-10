
package micycle.polygonmorphing.controls;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import micycle.polygonmorphing.controls.Resetable;

public class ClearButtonListener
implements ActionListener {
    private Vector rv = new Vector(3, 1);

    public void append(Resetable r) {
        this.rv.addElement(r);
    }

    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < this.rv.size(); ++i) {
            ((Resetable)this.rv.elementAt(i)).reset();
        }
    }
}

