package ui;

import java.awt.Dimension;

import com.amazon.kindle.kindlet.ui.KLabelMultiline;

public class KCommentArea extends KLabelMultiline {
  private static final long serialVersionUID = -8049705385905447528L;

  private int width;
  private int height;
  
  public KCommentArea(int width, int height) {
    super();
    this.setSize(width, height);
  }
  
  public Dimension getMaximumSize() {
    return new Dimension(width, height);
  }
  
  public Dimension getPreferredSize() {
    return new Dimension(width, height);
  }

}
