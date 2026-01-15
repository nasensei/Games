package TwentyFortyEight;

public class Cell {

    private int x;
    private int y;
    private int value;
    private static final float speed = 3;
    private float drawX;
    private float drawY;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.value = 0;
        this.drawX = x * App.CELLSIZE;
        this.drawY = y * App.CELLSIZE;
    }
    
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void place() {
        if (this.value == 0) {
            this.value = (App.random.nextInt(2)+1)*2;
        }
    }

    public int getValue() {
        return value;
    }

    public boolean isEmpty() {
        boolean empty = (value == 0);
        return empty;
    }

    public void setValue(int val) {
        this.value = val;
    }

    public void updatePos() {
        float targetX = x * App.CELLSIZE;
        float targetY = y * App.CELLSIZE;
        drawX += (targetX - drawX) / speed;
        drawY += (targetY - drawY) / speed;
    }

    /**
     * This draws the cell
     */
    public void draw(App app) {
        app.stroke(156, 139, 124); //color of the border
            if (app.mouseX > x*App.CELLSIZE && app.mouseX < (x+1)*App.CELLSIZE 
                && app.mouseY > y*App.CELLSIZE && app.mouseY < (y+1)*App.CELLSIZE) {
                app.fill(232, 207, 184);
            } else if (this.value == 2) {
                app.fill(237,227,217);
            } else if (this.value == 4){
                app.fill(235, 216, 182);
            } else if (this.value == 8) {
                app.fill(242,177,121);
            } else if (this.value == 16) {
                app.fill(244,149,100);
            } else if (this.value == 32) {
                app.fill(244,123,94);
            } else if (this.value == 64) {
                app.fill(246,93,59);
            } else if (this.value == 128) {
                app.fill(237,206,115);
            } else if (this.value == 256) {
                app.fill(237,204,99);
            } else if (this.value == 512) {
                app.fill(236,200,80);
            } else if (this.value == 1024) {
                app.fill(239,197,63);
            } else if (this.value == 2048) {
                app.fill(238,192,46);
            } else {
                app.fill(189,172,151);
            }
        app.rect(drawX, drawY, App.CELLSIZE, App.CELLSIZE, App.CELLSIZE * 0.15f);
        /*if (this.value > 0) {
            app.fill(117,109,112);
            app.text(String.valueOf(this.value), (x+0.4f)*App.CELLSIZE, (y+0.6f)*App.CELLSIZE);
        }*/
        if (this.value > 0) {
            app.fill(117, 109, 112);
            app.textAlign(app.CENTER, app.CENTER);
            app.textSize(40); // or scale with cell size if needed
        
            float centerX = drawX + App.CELLSIZE / 2f;
            float centerY = drawY + App.CELLSIZE / 2f;
        
            app.text(String.valueOf(this.value), centerX, centerY);
        }
        
    }
    
}
