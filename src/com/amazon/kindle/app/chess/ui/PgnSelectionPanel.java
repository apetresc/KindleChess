package com.amazon.kindle.app.chess.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.kwt.ui.KWTSelectableLabel;

import com.amazon.kindle.app.chess.Main;
import com.amazon.kindle.kindlet.ui.KBoxLayout;
import com.amazon.kindle.kindlet.ui.KLabelMultiline;
import com.amazon.kindle.kindlet.ui.KPages;
import com.amazon.kindle.kindlet.ui.KPanel;
import com.amazon.kindle.kindlet.ui.pages.PageProviders;
import com.codethesis.pgnparse.PGNParseException;

public class PgnSelectionPanel extends KPanel {
  private static final long serialVersionUID = -2392978560395832783L;

  private final Main main;
  private final KPages pgnListPages;

  private static final Logger log = Logger.getLogger(PgnSelectionPanel.class);

  private class PgnLabelActionListener implements ActionListener {
    private final File pgnFile;
    public PgnLabelActionListener(File pgnFile) {
      this.pgnFile = pgnFile;
    }
    public void actionPerformed(ActionEvent event) {
      try {
        GameSelectionPanel gameSelectionPanel = new GameSelectionPanel(main, pgnFile);
        main.setActivePanel(gameSelectionPanel);
      } catch (IOException e) {
        log.error(e);
      } catch (PGNParseException e) {
        log.error(e);
      }
    }
  }

  public PgnSelectionPanel(Main main, File[] pgnFiles, String[] pgnList) {
    super(new GridBagLayout());
    this.main = main;

    pgnListPages = new KPages(PageProviders.createKBoxLayoutProvider(KBoxLayout.Y_AXIS));
    pgnListPages.setFocusable(true);
    pgnListPages.setEnabled(true);
    pgnListPages.setPageKeyPolicy(KPages.PAGE_KEYS_LOCAL);

    for (int i = 0; i < pgnList.length; i++) {
      final KWTSelectableLabel pgnLabel = new KWTSelectableLabel(pgnList[i]);
      pgnLabel.setFocusable(true);
      pgnLabel.setEnabled(true);
      pgnLabel.setUnderlineStyle(KWTSelectableLabel.STYLE_DASHED);
      pgnLabel.addActionListener(new PgnLabelActionListener(pgnFiles[i]));
      pgnListPages.addItem(pgnLabel);
    }

    GridBagConstraints gc = new GridBagConstraints();
    gc.gridx = 0;
    gc.gridy = 0;
    gc.insets = new Insets(20, 20, 20, 20);
    gc.anchor = GridBagConstraints.NORTH;
    gc.weightx = 1.0;
    gc.weighty = 0.0;
    gc.fill = GridBagConstraints.HORIZONTAL;
    add(new KLabelMultiline("Select a PGN file to explore. This list comes from all of the files" 
        + " uploaded to the pgn/ subdirectory of this application"), gc);

    gc.gridy = 1;
    gc.weighty = 1.0;
    gc.fill = GridBagConstraints.BOTH;
    add(pgnListPages, gc);
    pgnListPages.first();
  }

  public void requestFocus() {
    pgnListPages.getComponent(0).requestFocus();
  }
}
