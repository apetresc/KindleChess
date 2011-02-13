package com.amazon.kindle.app.chess.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.kwt.ui.KWTSelectableLabel;

import com.amazon.kindle.app.chess.Main;
import com.amazon.kindle.kindlet.ui.KBoxLayout;
import com.amazon.kindle.kindlet.ui.KPages;
import com.amazon.kindle.kindlet.ui.KPanel;
import com.amazon.kindle.kindlet.ui.pages.PageProviders;
import com.codethesis.pgnparse.PGNGameStub;
import com.codethesis.pgnparse.PGNParseException;
import com.codethesis.pgnparse.PGNSource;

public class GameSelectionPanel extends KPanel {
  private static final long serialVersionUID = 3072075966182979415L;

  private final Main main;
  private final PGNSource pgnSource;
  private final KPages gameListPages;

  private static final Logger log = Logger.getLogger(GameSelectionPanel.class);

  private class GameLabelActionListener implements ActionListener {
    private final PGNGameStub gameStub;

    public GameLabelActionListener(PGNGameStub gameStub) {
      this.gameStub = gameStub;
    }

    public void actionPerformed(ActionEvent event) {
      try {
        main.loadPgn(pgnSource, gameStub);
        main.setActivePanel(Main.MAIN_PANEL);
      } catch (IOException e) {
        log.error(e);
      } catch (PGNParseException e) {
        log.error(e);
      }
    }
  }

  public GameSelectionPanel(Main main, File pgnFile) throws IOException, PGNParseException {
    super(new GridBagLayout());
    this.main = main;

    gameListPages = new KPages(PageProviders.createKBoxLayoutProvider(KBoxLayout.Y_AXIS));
    gameListPages.setFocusable(true);
    gameListPages.setEnabled(true);
    gameListPages.setPageKeyPolicy(KPages.PAGE_KEYS_LOCAL);

    pgnSource = new PGNSource(pgnFile);

    List pgnGameStubs = pgnSource.listGameStubs();
    Iterator it = pgnGameStubs.iterator();
    while (it.hasNext()) {
      PGNGameStub pgnGameStub = (PGNGameStub) it.next();
      String gameName = pgnGameStub.getTag("White") + " - " + pgnGameStub.getTag("Black");
      final KWTSelectableLabel gameLabel = new KWTSelectableLabel(gameName);
      gameLabel.setFocusable(true);
      gameLabel.setEnabled(true);
      gameLabel.setUnderlineStyle(KWTSelectableLabel.STYLE_DASHED);
      gameLabel.addActionListener(new GameLabelActionListener(pgnGameStub));
      gameListPages.addItem(gameLabel);
    }
    gameListPages.getComponent(0).requestFocus();

    GridBagConstraints gc = new GridBagConstraints();
    gc.gridx = 0;
    gc.gridy = 0;
    gc.insets = new Insets(20, 20, 20, 20);
    gc.anchor = GridBagConstraints.NORTH;
    gc.weightx = 1.0;
    gc.weighty = 1.0;
    gc.fill = GridBagConstraints.BOTH;
    add(gameListPages, gc);
    gameListPages.first();
  }

  public void requestFocus() {
    gameListPages.getComponent(0).requestFocus();
  }
}
