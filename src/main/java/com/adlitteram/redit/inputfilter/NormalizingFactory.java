package com.adlitteram.redit.inputfilter;

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
