package com.adlitteram.redit;

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
import com.adlitteram.jasmin.property.XProp;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

public class StyleManager {

    public static final int[] FONT_SIZES = {
        7, 8, 9, 10, 11, 12,
        14, 16, 18, 20, 22, 24,
        28, 32, 36, 40, 44, 48,
        56, 64, 72, 80, 88, 96};

    public static final String GROUP = "GroupAttribute";
    public static final String VISIBLE = "VisibleAttribute";
    // Header
    public static final String HEADER_GROUP = "titraille";
    public static final String HEADING_STYLE = "surtitre";
    public static final String TITLE_STYLE = "titre";
    public static final String SUBTITLE_STYLE = "soustitre";
    public static final String CHAPO_STYLE = "chapo";
    // Body
    public static final String BODY_GROUP = "texte";
    public static final String BODY_STYLE = "texte";
    public static final String SUBHEADING_STYLE = "intertitre";
    public static final String THROW_STYLE = "relance";
    // Formats
    public static final String RESULT_STYLE = "resultats";
    public static final String SIGNATURE_STYLE = "signature";
    public static final String NOTE_STYLE = "note";
    // Photo
    public static final String PHOTO_GROUP = "photo-groupe";
    public static final String CAPTION_STYLE = "photo-legende";
    // Formats (all styles)
    public static final String REGULAR_STYLE = "regular";
    public static final String BOLD_STYLE = "bold";
    public static final String ITALIC_STYLE = "italic";

    private static Style defaultStyle;

    private final AppManager appManager;
    private final StyleContext styleContext;
    private final ArrayList<StyleGroup> groupList;

    // TODO : import from configuration file
    public StyleManager(AppManager appManager) {
        this.appManager = appManager;

        styleContext = new StyleContext();
        groupList = new ArrayList<>();

        // Set paragraph styles (logical styles)
        defaultStyle = styleContext.getStyle(StyleContext.DEFAULT_STYLE);

        // Heading
        Style headingStyle = styleContext.addStyle(HEADING_STYLE, defaultStyle);
        StyleConstants.setBold(headingStyle, true);
        StyleConstants.setAlignment(headingStyle, StyleConstants.ALIGN_LEFT);
        StyleConstants.setSpaceBelow(headingStyle, 5);
        StyleConstants.setFontSize(headingStyle, XProp.getInt("Style." + HEADING_STYLE + ".FontSize", 18));
        StyleConstants.setForeground(headingStyle, new Color(38, 76, 153));

        Style titleStyle = styleContext.addStyle(TITLE_STYLE, defaultStyle);
        StyleConstants.setBold(titleStyle, true);
        StyleConstants.setSpaceBelow(titleStyle, 5);
        StyleConstants.setFontSize(titleStyle, XProp.getInt("Style." + TITLE_STYLE + ".FontSize", 32));
        StyleConstants.setAlignment(titleStyle, StyleConstants.ALIGN_CENTER);
        StyleConstants.setForeground(titleStyle, new Color(55, 110, 219));

        Style subtitleStyle = styleContext.addStyle(SUBTITLE_STYLE, defaultStyle);
        StyleConstants.setAlignment(subtitleStyle, StyleConstants.ALIGN_LEFT);
        StyleConstants.setBold(subtitleStyle, true);
        StyleConstants.setSpaceBelow(subtitleStyle, 5);
        StyleConstants.setFontSize(subtitleStyle, XProp.getInt("Style." + SUBTITLE_STYLE + ".FontSize", 18));
        StyleConstants.setForeground(subtitleStyle, new Color(58, 116, 232));

        Style chapoStyle = styleContext.addStyle(CHAPO_STYLE, defaultStyle);
        StyleConstants.setAlignment(chapoStyle, StyleConstants.ALIGN_LEFT);
        StyleConstants.setBold(chapoStyle, true);
        StyleConstants.setSpaceBelow(chapoStyle, 5);
        StyleConstants.setFontSize(chapoStyle, XProp.getInt("Style." + CHAPO_STYLE + ".FontSize", 18));
        StyleConstants.setForeground(chapoStyle, new Color(91, 19, 199));

        StyleGroup group = new StyleGroup(HEADER_GROUP);
        group.addStyle(headingStyle);
        group.addStyle(titleStyle);
        group.addStyle(subtitleStyle);
        group.addStyle(chapoStyle);
        groupList.add(group);

        // Body
        Style textStyle = styleContext.addStyle(BODY_STYLE, defaultStyle);
        textStyle.addAttribute(VISIBLE, Boolean.FALSE);  // don't output this tag
        StyleConstants.setFirstLineIndent(textStyle, 10);
        StyleConstants.setFontSize(textStyle, XProp.getInt("Style." + BODY_STYLE + ".FontSize", 14));
        StyleConstants.setSpaceAbove(textStyle, 5);
        StyleConstants.setSpaceBelow(textStyle, 5);

        Style subHeadingStyle = styleContext.addStyle(SUBHEADING_STYLE, defaultStyle);
        StyleConstants.setBold(subHeadingStyle, true);
        StyleConstants.setFontSize(subHeadingStyle, XProp.getInt("Style." + SUBHEADING_STYLE + ".FontSize", 14));
        StyleConstants.setSpaceAbove(subHeadingStyle, 5);
        StyleConstants.setSpaceBelow(subHeadingStyle, 5);
        StyleConstants.setLeftIndent(subHeadingStyle, 75);
        StyleConstants.setRightIndent(subHeadingStyle, 75);
        StyleConstants.setAlignment(subHeadingStyle, StyleConstants.ALIGN_CENTER);

        Style throwStyle = styleContext.addStyle(THROW_STYLE, defaultStyle);
        StyleConstants.setBold(throwStyle, true);
        StyleConstants.setFontSize(throwStyle, XProp.getInt("Style." + THROW_STYLE + ".FontSize", 18));
        StyleConstants.setSpaceAbove(throwStyle, 5);
        StyleConstants.setSpaceBelow(throwStyle, 5);
        StyleConstants.setLeftIndent(throwStyle, 75);
        StyleConstants.setRightIndent(throwStyle, 75);
        StyleConstants.setAlignment(throwStyle, StyleConstants.ALIGN_CENTER);

        group = new StyleGroup(BODY_GROUP);
        group.addStyle(textStyle);
        group.addStyle(subHeadingStyle);
        group.addStyle(throwStyle);
        groupList.add(group);

        // Photo
        Style captionStyle = styleContext.addStyle(CAPTION_STYLE, defaultStyle);
        StyleConstants.setFontSize(captionStyle, XProp.getInt("Style." + CAPTION_STYLE + ".FontSize", 14));
        StyleConstants.setForeground(captionStyle, new Color(204, 51, 51));
        StyleConstants.setSpaceAbove(captionStyle, 15);
        StyleConstants.setSpaceBelow(captionStyle, 5);

        group = new StyleGroup(PHOTO_GROUP);
        group.addStyle(captionStyle);
        groupList.add(group);

        // Formats (Character Style)
        Style resultStyle = styleContext.addStyle(RESULT_STYLE, defaultStyle);
        StyleConstants.setFontFamily(resultStyle, "Monospaced");
        StyleConstants.setFontSize(resultStyle, XProp.getInt("Style." + RESULT_STYLE + ".FontSize", 14));

        Style signatureStyle = styleContext.addStyle(SIGNATURE_STYLE, defaultStyle);
        StyleConstants.setItalic(signatureStyle, true);
        StyleConstants.setBold(signatureStyle, true);
        StyleConstants.setFontSize(signatureStyle, XProp.getInt("Style." + SIGNATURE_STYLE + ".FontSize", 14));
        StyleConstants.setForeground(signatureStyle, Color.DARK_GRAY);

        Style noteStyle = styleContext.addStyle(NOTE_STYLE, defaultStyle);
        StyleConstants.setItalic(noteStyle, true);
        StyleConstants.setFontSize(noteStyle, XProp.getInt("Style." + NOTE_STYLE + ".FontSize", 12));
        StyleConstants.setForeground(noteStyle, Color.DARK_GRAY);
    }

    public StyleContext getStyleContext() {
        return styleContext;
    }

    public ArrayList<StyleGroup> getGroupList() {
        return groupList;
    }

    public Style getStyle(String name) {
        return styleContext.getStyle(name);
    }

    private boolean canChangeFontSize(RDocument document, int n) {
        for (Enumeration names = styleContext.getStyleNames(); names.hasMoreElements();) {
            String name = (String) names.nextElement();
            Style style = styleContext.getStyle(name);
            if (style == defaultStyle) {
                continue;
            }
            int fontSize = StyleConstants.getFontSize(style);
            int fs = (n > 0) ? nextFontSize(fontSize) : prevFontSize(fontSize);
            if (fontSize == fs) {
                return false;
            }
        }
        return (document == null) ? true : document.canChangeFontSize(n);
    }

    public void changeFontSize(RDocument document, int n) {

        for (Enumeration names = styleContext.getStyleNames(); names.hasMoreElements();) {
            String name = (String) names.nextElement();
            Style style = styleContext.getStyle(name);
            if (style != defaultStyle) {
                int fontSize = StyleConstants.getFontSize(style);
                int fs = (n > 0) ? nextFontSize(fontSize) : prevFontSize(fontSize);
                XProp.put("Style." + style.getName() + ".FontSize", fs);
                StyleConstants.setFontSize(style, fs);
            }
        }

        if (document != null) {
            document.changeFontSize(n);
        }
    }

    public void increaseFontSize(RDocument document) {
        if (canChangeFontSize(document, 1)) {
            changeFontSize(document, 1);
        }
    }

    public void decreaseFontSize(RDocument document) {
        if (canChangeFontSize(document, -1)) {
            changeFontSize(document, -1);
        }
    }

    public static int nextFontSize(int size) {
        for (int s : FONT_SIZES) {
            if (s > size) {
                return s;
            }
        }
        return size;
    }

    public static int prevFontSize(int size) {
        for (int i = FONT_SIZES.length - 1; i >= 0; i--) {
            if (FONT_SIZES[i] < size) {
                return FONT_SIZES[i];
            }
        }
        return size;
    }

    public static void setParagraphStyle(StyledDocument doc, int pos1, int pos2, Style style) {
        int pos = Math.min(pos1, pos2);
        int maxPos = Math.max(pos1, pos2);
        SimpleAttributeSet emptySet = new SimpleAttributeSet();

        if (pos == maxPos) {
            Style paraSyle = doc.getLogicalStyle(pos);
            if (!style.equals(paraSyle)) {
                Element paragraph = doc.getParagraphElement(pos);
                int startOffset = paragraph.getStartOffset();
                int endOffset = paragraph.getEndOffset();
                doc.setCharacterAttributes(startOffset, endOffset - startOffset, emptySet, true);
                doc.setLogicalStyle(pos, style);
            }
        }
        else {
            while (pos < maxPos) {
                Element paragraph = doc.getParagraphElement(pos);
                if (paragraph == null) {
                    break;
                }
                int startOffset = paragraph.getStartOffset();
                int endOffset = paragraph.getEndOffset();

                Style paraSyle = doc.getLogicalStyle(pos);
                if (!style.equals(paraSyle)) {
                    doc.setCharacterAttributes(startOffset, endOffset - startOffset, emptySet, true);
                    doc.setLogicalStyle(pos, style);
                }

                if (endOffset <= pos) {
                    break;
                }
                pos = endOffset;
            }
        }
    }

    public static Style getLogicalStyle(StyledDocument doc, int pos1, int pos2) {
        int pos = Math.min(pos1, pos2);
        int maxPos = Math.max(pos1, pos2);

        if (pos == maxPos) {
            return doc.getLogicalStyle(pos);
        }
        else {
            Style currentSyle = null;
            while (pos < maxPos) {
                Element paragraph = doc.getParagraphElement(pos);
                if (paragraph == null) {
                    break;
                }

                Style style = doc.getLogicalStyle(pos);
                if (currentSyle != null && !currentSyle.equals(style)) {
                    return null;
                }
                currentSyle = style;

                int endOffset = paragraph.getEndOffset();
                if (endOffset <= pos) {
                    break;
                }
                pos = endOffset;
            }
            return currentSyle;
        }
    }

    public static void setCharacterStyle(StyledDocument doc, int pos1, int pos2, AttributeSet attr, boolean replace) {
        if (pos1 == pos2) {
            Element paragraph = doc.getParagraphElement(pos1);
            int offset = paragraph.getStartOffset();
            int length = paragraph.getEndOffset() - offset;
            doc.setCharacterAttributes(offset, length, attr, replace);
        }
        else {
            int offset = Math.min(pos1, pos2);
            int length = Math.max(pos1, pos2) - offset;
            doc.setCharacterAttributes(offset, length, attr, replace);
        }
    }

    public static Style getCharacterStyle(StyledDocument doc, int pos) {
        Element element = doc.getCharacterElement(pos);
        return getCharacterStyle(doc, element.getAttributes());
    }

    public static Style getCharacterStyle(StyledDocument doc, AttributeSet attr) {
        String name = (String) attr.getAttribute(StyleConstants.NameAttribute);
        Style style = doc.getStyle(name);
        if (style != null && !style.isDefined(StyleManager.GROUP)) {
            return style;
        }
        return null;
    }

    public static boolean checkTitleStyle(StyledDocument doc) {
        int pos = 0;
        int maxPos = doc.getLength();
        int state = 0;

        while (pos < maxPos) {
            Element paragraph = doc.getParagraphElement(pos);
            if (paragraph == null) {
                break;
            }
            Style style = doc.getLogicalStyle(pos);

            switch (state) {
                case 0:
                    if (HEADING_STYLE.equals(style.getName())) {
                        state = 0;
                    }
                    else {
                        state = 1;
                    }
                    break;
                case 1:
                    if (TITLE_STYLE.equals(style.getName())) {
                        return false;
                    }
                    break;
            }

            int endOffset = paragraph.getEndOffset();
            if (endOffset <= pos) {
                break;
            }
            pos = endOffset;
        }

        return true;
    }
}
