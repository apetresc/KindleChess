package com.amazon.kindle.app.chess.ui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import org.kwt.ui.KWTProgressBar;

import com.amazon.kindle.app.chess.ChessBoard;
import com.amazon.kindle.kindlet.KindletContext;
import com.amazon.kindle.kindlet.ui.KLabel;
import com.amazon.kindle.kindlet.ui.KPanel;

public class MainPanel extends KPanel {
  private static final long serialVersionUID = -952153975203018597L;

  private final KLabel titleLabel;
  private final KLabel descriptionLabel;
  private final KChessBoardComponent boardComponent;
  private final KCommentArea commentComponent;
  private final KWTProgressBar progressBar;

  public MainPanel(KindletContext context, ChessBoard board) {
    super(new GridBagLayout());
    GridBagConstraints gc = new GridBagConstraints();

    titleLabel = new KLabel();
    titleLabel.setFont(new Font(null, Font.BOLD, 25));
    gc.gridx = 0;
    gc.gridy = 0;
    gc.weighty = 0.0;
    gc.anchor = GridBagConstraints.NORTH;
    add(titleLabel, gc);
    
    descriptionLabel = new KLabel();
    descriptionLabel.setFont(new Font(null, Font.BOLD, 18));
    gc.gridy = 1;
    add(descriptionLabel, gc);
    
    boardComponent = new KChessBoardComponent(context, board, 75, true, false, false);
    boardComponent.setFocusable(true);
    gc.gridy = 2;
    gc.insets = new Insets(0, 10, 0, 0);
    add(boardComponent, gc);
    
    commentComponent = new KCommentArea(ChessBoard.SIZE * boardComponent.getSquareSize() + 4, 200);
    commentComponent.setFocusable(false);
    gc.gridy = 3;
    gc.weighty = 1.0;
    gc.fill = GridBagConstraints.BOTH;
    gc.insets = new Insets(0, 10, 0, 10);
    add(commentComponent, gc);

    progressBar = new KWTProgressBar();
    progressBar.setLabelStyle(KWTProgressBar.STYLE_NONE);
    gc.gridy = 4;
    gc.insets = new Insets(0, 10, 20, 10);
    gc.fill = GridBagConstraints.HORIZONTAL;
    gc.anchor = GridBagConstraints.SOUTH;
    add(progressBar, gc);    
  }


  public KCommentArea getCommentArea() {
    return commentComponent;
  }

  public KChessBoardComponent getChessBoardComponent() {
    return boardComponent;
  }
  
  public KWTProgressBar getProgressBar() {
    return progressBar;
  }

}
