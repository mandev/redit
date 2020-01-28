package com.adlitteram.redit.draganddrop;

/*-
 * #%L
 * rEdit
 * %%
 * Copyright (C) 2009 - 2019 mandev
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
import java.awt.Image;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import javax.swing.JComponent;

public class ImageSelection implements Transferable, ClipboardOwner {

    private static final DataFlavor[] FLAVORS = {DataFlavor.imageFlavor};
    private final Image image;

    public ImageSelection(Image img) {
        image = img;
    }

    @Override
    public Object getTransferData(DataFlavor flavor) {
        return (FLAVORS[0].equals(flavor)) ? image : null;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return FLAVORS;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return FLAVORS[0].equals(flavor);
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
    }

    // Export Data from the component
    public static Transferable createTransferable(JComponent comp) {
        return null;
    }

    // Paste Data to the component
    public static boolean importData(JComponent comp, Transferable t) {
        return false;
    }

    // Return the import capabilitiy
    public static boolean canImport(JComponent comp, DataFlavor flav[]) {
        for (int i = 0, n = flav.length; i < n; i++) {
            if (FLAVORS[0].equals(flav[i])) {
                return true;
            }
        }
        return false;
    }
}
