package Game;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

//mouse adapter - window events registered, keep track
public class MouseListener extends MouseAdapter {
    public boolean mousePressed = false;
    public boolean mouseDragged = false;
    public int mouseButton = -1;
    //current poz
    public float x = - 1.0f;
    public float y = -1.0f;

    //how much distance travelled since last frame
    public float dx;
    public float dy;

    @Override
    public void mousePressed(MouseEvent mouseEvent){
        this.mousePressed = true;
        this.mouseButton = mouseEvent.getButton();
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent){
        this.mousePressed = false;
        this.mouseDragged = false;
        //if no dragging then no distance to calc..
        this.dx = 0;
        this.dy = 0;
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent){
        this.x = mouseEvent.getX();
        this.y = mouseEvent.getY();
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent){
        this.mouseDragged = true;
        this.dx = mouseEvent.getX() - this.x;       //distance travelled for x
        this.dy = mouseEvent.getY() - this.y;       //distance travelled for y
    }

}
