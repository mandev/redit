package com.adlitteram.redit;

public class IptcCountry {

   private final String code;
   private final String name;

   public IptcCountry(String c, String n) {
      code = c.trim();
      name = n.trim();
   }

   @Override
   public String toString() {
      return (code.length() > 0) ? code + " (" + name + ")" : "";
   }

   public String getCode() {
      return code;
   }

   public String getName() {
      return name;
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      }

      if (obj instanceof IptcCountry) {
         return (code.equals(((IptcCountry) obj).getCode()));
      }

      return false;
   }

   @Override
   public int hashCode() {
      int hash = 5;
      hash = 43 * hash + (this.code != null ? this.code.hashCode() : 0);
      hash = 43 * hash + (this.name != null ? this.name.hashCode() : 0);
      return hash;
   }

   public static IptcCountry getInstanceCode(String code) {
      for (IptcCountry ic : COUNTRIES) {
         if (ic.getCode().equals(code)) {
            return ic;
         }
      }
      return new IptcCountry("", "");
   }

   public static IptcCountry getInstanceName(String name) {
      for (IptcCountry ic : COUNTRIES) {
         if (ic.getName().equals(name)) {
            return ic;
         }
      }
      return new IptcCountry("", "");
   }
   public static final IptcCountry[] COUNTRIES = {
      new IptcCountry("FRA", "France"),
      new IptcCountry("", ""),
      new IptcCountry("BEL", "Belgium"),
      new IptcCountry("CHE", "Switzerland"),
      new IptcCountry("DEU", "Germany"),
      new IptcCountry("ESP", "Spain"),
      new IptcCountry("GBR", "Great Britain"),
      new IptcCountry("GRC", "Greece"),
      new IptcCountry("ITA", "Italy"),
      new IptcCountry("NLD", "Netherlands"),
      new IptcCountry("NOR", "Norway"),
      new IptcCountry("SWE", "Sweden"),
      new IptcCountry("", ""),
      new IptcCountry("AFG", "Afghanistan"),
      new IptcCountry("ALB", "Albania"),
      new IptcCountry("DZA", "Algeria"),
      new IptcCountry("ASM", "American Samoa"),
      new IptcCountry("AND", "Andorra"),
      new IptcCountry("AGO", "Angola"),
      new IptcCountry("AIA", "Anguilla"),
      new IptcCountry("ATA", "Antarctica"),
      new IptcCountry("ATG", "Antigua & Barbuda"),
      new IptcCountry("ARG", "Argentina"),
      new IptcCountry("ARM", "Armenia"),
      new IptcCountry("ABW", "Aruba"),
      new IptcCountry("AUS", "Australia"),
      new IptcCountry("AUT", "Austria"),
      new IptcCountry("AZE", "Azerbaijan"),
      new IptcCountry("BHS", "Bahamas"),
      new IptcCountry("BHR", "Bahrain"),
      new IptcCountry("BGD", "Bangladesh"),
      new IptcCountry("BRB", "Barbados"),
      new IptcCountry("BLR", "Belarus"),
      new IptcCountry("BEL", "Belgium"),
      new IptcCountry("BLZ", "Belize"),
      new IptcCountry("BEN", "Benin"),
      new IptcCountry("BMU", "Bermuda"),
      new IptcCountry("BTN", "Bhutan"),
      new IptcCountry("BOL", "Bolivia"),
      new IptcCountry("BIH", "Bosnia Herzegowina"),
      new IptcCountry("BWA", "Botswana"),
      new IptcCountry("BVT", "Bouvet Island"),
      new IptcCountry("BRA", "Brazil"),
      new IptcCountry("IOT", "British Indian Ocean Territory"),
      new IptcCountry("VGB", "British Virgin Islands"),
      new IptcCountry("BRN", "Brunei Darussalam"),
      new IptcCountry("BGR", "Bulgaria"),
      new IptcCountry("BFA", "Burkina Faso"),
      new IptcCountry("BDI", "Burundi"),
      new IptcCountry("KHM", "Cambodia"),
      new IptcCountry("CMR", "Cameroon"),
      new IptcCountry("CAN", "Canada"),
      new IptcCountry("CPV", "Cape Verde"),
      new IptcCountry("CYM", "Cayman Islands"),
      new IptcCountry("CAF", "Central African Republic"),
      new IptcCountry("TCD", "Chad"),
      new IptcCountry("CHL", "Chile"),
      new IptcCountry("CHN", "China"),
      new IptcCountry("CXR", "Christmas Island"),
      new IptcCountry("CCK", "Cocos Islands"),
      new IptcCountry("COL", "Colombia"),
      new IptcCountry("COM", "Comoros"),
      new IptcCountry("COG", "Congo"),
      new IptcCountry("COK", "Cook Islands"),
      new IptcCountry("CRI", "Costa Rica"),
      new IptcCountry("CIV", "Ivory Coast"),
      new IptcCountry("CUB", "Cuba"),
      new IptcCountry("CYP", "Cyprus"),
      new IptcCountry("CZE", "Czech Republic"),
      new IptcCountry("DNK", "Denmark"),
      new IptcCountry("DJI", "Djibouti"),
      new IptcCountry("DMA", "Dominica"),
      new IptcCountry("DOM", "Dominican Republic"),
      new IptcCountry("TMP", "East Timor"),
      new IptcCountry("ECU", "Ecuador"),
      new IptcCountry("EGY", "Egypt"),
      new IptcCountry("SLV", "El Salvador"),
      new IptcCountry("GNQ", "Equatorial Guinea"),
      new IptcCountry("ERI", "Eritrea"),
      new IptcCountry("EST", "Estonia"),
      new IptcCountry("ETH", "Ethiopia"),
      new IptcCountry("FRO", "Faeroe Islands"),
      new IptcCountry("FLK", "Falkland Islands"),
      new IptcCountry("FJI", "Fiji"),
      new IptcCountry("FIN", "Finland"),
      new IptcCountry("FRA", "France"),
      new IptcCountry("GUF", "French Guiana"),
      new IptcCountry("PYF", "French Polynesia"),
      new IptcCountry("ATF", "French Southern Territories"),
      new IptcCountry("GAB", "Gabon"),
      new IptcCountry("GMB", "Gambia"),
      new IptcCountry("GEO", "Georgia"),
      new IptcCountry("DEU", "Germany"),
      new IptcCountry("GHA", "Ghana"),
      new IptcCountry("GIB", "Gibraltar"),
      new IptcCountry("GRC", "Greece"),
      new IptcCountry("GRL", "Greenland"),
      new IptcCountry("GRD", "Grenada"),
      new IptcCountry("GLP", "Guadaloupe"),
      new IptcCountry("GUM", "Guam"),
      new IptcCountry("GTM", "Guatemala"),
      new IptcCountry("GIN", "Guinea"),
      new IptcCountry("GNB", "Guinea-Bissau"),
      new IptcCountry("GUY", "Guyana"),
      new IptcCountry("HTI", "Haiti"),
      new IptcCountry("HMD", "Heard & McDonald Islands"),
      new IptcCountry("HND", "Honduras"),
      new IptcCountry("HKG", "Hong Kong"),
      new IptcCountry("HRV", "Croatia"),
      new IptcCountry("HUN", "Hungary"),
      new IptcCountry("ISL", "Iceland"),
      new IptcCountry("IND", "India"),
      new IptcCountry("IDN", "Indonesia"),
      new IptcCountry("IRN", "Iran"),
      new IptcCountry("IRQ", "Iraq"),
      new IptcCountry("IRL", "Ireland"),
      new IptcCountry("ISR", "Israel"),
      new IptcCountry("ITA", "Italy"),
      new IptcCountry("JAM", "Jamaica"),
      new IptcCountry("JPN", "Japan"),
      new IptcCountry("JOR", "Jordan"),
      new IptcCountry("KAZ", "Kazakhstan"),
      new IptcCountry("KEN", "Kenya"),
      new IptcCountry("KIR", "Kiribati"),
      new IptcCountry("PRK", "Korea"),
      new IptcCountry("KOR", "Korea"),
      new IptcCountry("KWT", "Kuwait"),
      new IptcCountry("KGZ", "Kyrgyz Republic"),
      new IptcCountry("LAO", "Laos"),
      new IptcCountry("LVA", "Latvia"),
      new IptcCountry("LBN", "Lebanon"),
      new IptcCountry("LSO", "Lesotho"),
      new IptcCountry("LBR", "Liberia"),
      new IptcCountry("LBY", "Libyan"),
      new IptcCountry("LIE", "Liechtenstein"),
      new IptcCountry("LTU", "Lithuania"),
      new IptcCountry("LUX", "Luxembourg"),
      new IptcCountry("MAC", "Macau"),
      new IptcCountry("MKD", "Macedonia"),
      new IptcCountry("MDG", "Madagascar"),
      new IptcCountry("MWI", "Malawi"),
      new IptcCountry("MYS", "Malaysia"),
      new IptcCountry("MDV", "Maldives"),
      new IptcCountry("MLI", "Mali"),
      new IptcCountry("MLT", "Malta"),
      new IptcCountry("MHL", "Marshall Islands"),
      new IptcCountry("MTQ", "Martinique"),
      new IptcCountry("MRT", "Mauritania"),
      new IptcCountry("MUS", "Mauritius"),
      new IptcCountry("MYT", "Mayotte"),
      new IptcCountry("MEX", "Mexico"),
      new IptcCountry("FSM", "Micronesia"),
      new IptcCountry("MDA", "Moldova"),
      new IptcCountry("MCO", "Monaco"),
      new IptcCountry("MNG", "Mongolia"),
      new IptcCountry("MSR", "Montserrat"),
      new IptcCountry("MAR", "Morocco, Kingdom of"),
      new IptcCountry("MOZ", "Mozambique"),
      new IptcCountry("MMR", "Myanmar"),
      new IptcCountry("NAM", "Namibia"),
      new IptcCountry("NRU", "Nauru"),
      new IptcCountry("NPL", "Nepal"),
      new IptcCountry("ANT", "Netherlands Antilles"),
      new IptcCountry("NLD", "Netherlands"),
      new IptcCountry("NCL", "New Caledonia"),
      new IptcCountry("NZL", "New Zealand"),
      new IptcCountry("NIC", "Nicaragua"),
      new IptcCountry("NER", "Niger"),
      new IptcCountry("NGA", "Nigeria"),
      new IptcCountry("NIU", "Niue"),
      new IptcCountry("NFK", "Norfolk Island"),
      new IptcCountry("MNP", "Northern Mariana Islands"),
      new IptcCountry("NOR", "Norway"),
      new IptcCountry("OMN", "Oman"),
      new IptcCountry("PAK", "Pakistan"),
      new IptcCountry("PLW", "Palau"),
      new IptcCountry("PAN", "Panama"),
      new IptcCountry("PNG", "Papua New Guinea"),
      new IptcCountry("PRY", "Paraguay"),
      new IptcCountry("PER", "Peru"),
      new IptcCountry("PHL", "Philippines"),
      new IptcCountry("PCN", "Pitcairn Island"),
      new IptcCountry("POL", "Poland"),
      new IptcCountry("PRT", "Portugal"),
      new IptcCountry("PRI", "Puerto Rico"),
      new IptcCountry("QAT", "Qatar"),
      new IptcCountry("REU", "Reunion"),
      new IptcCountry("ROM", "Romania"),
      new IptcCountry("RUS", "Russian Federation"),
      new IptcCountry("RWA", "Rwanda"),
      new IptcCountry("KNA", "Saint Kitts and Nevis"),
      new IptcCountry("LCA", "Saint Lucia"),
      new IptcCountry("VCT", "Saint Vincent & Grenadines"),
      new IptcCountry("WSM", "Samoa"),
      new IptcCountry("SMR", "San Marino"),
      new IptcCountry("STP", "Sao Tome & Principe"),
      new IptcCountry("SAU", "Saudi Arabia"),
      new IptcCountry("SEN", "Senegal"),
      new IptcCountry("SYC", "Seychelles"),
      new IptcCountry("SLE", "Sierra Leone"),
      new IptcCountry("SGP", "Singapore"),
      new IptcCountry("SVK", "Slovakia"),
      new IptcCountry("SVN", "Slovenia"),
      new IptcCountry("SLB", "Solomon Islands"),
      new IptcCountry("SOM", "Somalia"),
      new IptcCountry("ZAF", "South Africa"),
      new IptcCountry("SGS", "South Georgia & Sandwich"),
      new IptcCountry("ESP", "Spain"),
      new IptcCountry("LKA", "Sri Lanka"),
      new IptcCountry("SHN", "St. Helena"),
      new IptcCountry("SPM", "St. Pierre & Miquelon"),
      new IptcCountry("SDN", "Sudan"),
      new IptcCountry("SUR", "Suriname"),
      new IptcCountry("SJM", "Svalbard & Jan Mayen"),
      new IptcCountry("SWZ", "Swaziland"),
      new IptcCountry("SWE", "Sweden"),
      new IptcCountry("CHE", "Switzerland"),
      new IptcCountry("SYR", "Syrian Arab Republic"),
      new IptcCountry("TWN", "Taiwan"),
      new IptcCountry("TJK", "Tajikistan"),
      new IptcCountry("TZA", "Tanzania"),
      new IptcCountry("THA", "Thailand"),
      new IptcCountry("TGO", "Togo"),
      new IptcCountry("TKL", "Tokelau"),
      new IptcCountry("TON", "Tonga"),
      new IptcCountry("TTO", "Trinidad & Tobago"),
      new IptcCountry("TUN", "Tunisia"),
      new IptcCountry("TUR", "Turkey"),
      new IptcCountry("TKM", "Turkmenistan"),
      new IptcCountry("TCA", "Turks & Caicos Islands"),
      new IptcCountry("TUV", "Tuvalu"),
      new IptcCountry("VIR", "US Virgin Islands"),
      new IptcCountry("UGA", "Uganda"),
      new IptcCountry("UKR", "Ukraine"),
      new IptcCountry("ARE", "United Arab Emirates"),
      new IptcCountry("GBR", "Great Britain"),
      new IptcCountry("UMI", "United States Minor Islands"),
      new IptcCountry("USA", "United States"),
      new IptcCountry("URY", "Uruguay"),
      new IptcCountry("UZB", "Uzbekistan"),
      new IptcCountry("VUT", "Vanuatu"),
      new IptcCountry("VAT", "Vatican City State"),
      new IptcCountry("VEN", "Venezuela"),
      new IptcCountry("VNM", "Viet Nam"),
      new IptcCountry("WLF", "Wallis & Futuna Islands"),
      new IptcCountry("ESH", "Western Sahara"),
      new IptcCountry("YEM", "Yemen"),
      new IptcCountry("YUG", "Yugoslavia"),
      new IptcCountry("ZAR", "Zaire"),
      new IptcCountry("ZMB", "Zambia"),
      new IptcCountry("ZWE", "Zimbabwe")
   };
}
