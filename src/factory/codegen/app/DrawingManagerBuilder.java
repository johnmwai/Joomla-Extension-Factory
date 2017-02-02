package factory.codegen.app;

import javax.swing.ImageIcon;


public class DrawingManagerBuilder {
    private ImageIcon drawinIcon;
    private AllSystems allSystems;

    public DrawingManagerBuilder() {
    }

    public DrawingManagerBuilder setDrawinIcon(ImageIcon drawinIcon) {
        this.drawinIcon = drawinIcon;
        return this;
    }
    public DrawingManagerBuilder setAllSystems(AllSystems allSystems) {
        this.allSystems = allSystems;
        return this;
    }

    public DrawingManager createDrawingManager() {
        return new DrawingManager(drawinIcon, allSystems);
    }
    
}
