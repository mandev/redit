package com.adlitteram.redit.inputfilter;

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
import com.adlitteram.redit.Column;
import com.adlitteram.redit.Group;
import com.adlitteram.redit.IptcField;
import com.adlitteram.redit.User;
import org.xml.sax.Attributes;

public class ResponseUserHandler extends ResponseHandler {

   private User user;
   private Group group;
   private Column column;
   private IptcField iptcField;
   //
   private boolean isInArticleColumns = false;

   public ResponseUserHandler() {
      super(new ResponseUser());
   }

   @Override
   public void startElement(String uri, String local, String raw, Attributes attrs) {
      if (null != raw) {
         switch (raw) {
            case "user":
               user = new User();
               break;
            case "group":
               group = new Group();
               break;
            case "articleColumns":
               isInArticleColumns = true;
               break;
            case "column":
               column = new Column();
               break;
            case "iptcField":
               iptcField = new IptcField();
               break;
         }
      }
      super.startElement(uri, local, raw, attrs);
   }

   @Override
   public void endElement(String uri, String local, String raw) {
      if (null != raw) {
         switch (raw) {
            case "user":
               ((ResponseUser) response).setUser(user);
               break;
            case "userName":
               user.setUserName(buffer.toString());
               break;
            case "unitName":
               user.setUnitName(buffer.toString());
               break;
            case "unitDescription":
               user.setUnitDescription(buffer.toString());
               break;
            case "role":
               user.setRole(buffer.toString());
               break;
            case "group":
               user.setGroup(group);
               break;
            case "groupName":
               group.setGroupName(buffer.toString());
               break;
            case "groupDescription":
               group.setGroupDescription(buffer.toString());
               break;
            case "accessRedaction":
               group.setAccessRedaction(Boolean.valueOf(buffer.toString()));
               break;
            case "accessPictureAssociation":
               group.setAccessPictureAssociation(Boolean.valueOf(buffer.toString()));
               break;
            case "articleColumns":
               isInArticleColumns = false;
               break;
            case "column":
               if (isInArticleColumns) {
                  group.addColumn(column);
               }
               break;
            case "columnName":
               column.setColumnName(buffer.toString());
               break;
            case "columnDescription":
               column.setColumnDescription(buffer.toString());
               break;
            case "paper":
               column.setPaper(buffer.toString());
               break;
            case "paperDescription":
               column.setPaperDescription(buffer.toString());
               break;
            case "textStorageDirectory":
               column.setTextStorageDirectory(buffer.toString());
               break;
            case "pictureStorageDirectory":
               column.setPictureStorageDirectory(buffer.toString());
               break;
            case "iptcField":
               column.addIptcField(iptcField);
               break;
            case "label":
               iptcField.setLabel(buffer.toString());
               break;
            case "value":
               iptcField.setValue(buffer.toString());
               break;
            default:
               super.endElement(uri, local, raw);
               break;
         }
      }
   }
}
