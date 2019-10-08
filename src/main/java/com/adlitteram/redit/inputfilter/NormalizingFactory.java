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
import com.adlitteram.jasmin.utils.StrUtils;
import nu.xom.Attribute;
import nu.xom.NodeFactory;
import nu.xom.Nodes;

public class NormalizingFactory extends NodeFactory {

   @Override
   public Nodes makeText(String data) {
      data = StrUtils.stripXmlSpace(data);
      return data.length() == 0 ? new Nodes() : super.makeText(data);
   }

   @Override
   public Nodes makeAttribute(String name, String URI, String value, Attribute.Type type) {
      value = StrUtils.stripXmlSpace(value);
      return super.makeAttribute(name, URI, value, type);
   }
}
