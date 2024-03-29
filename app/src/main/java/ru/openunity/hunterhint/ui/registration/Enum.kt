package ru.openunity.hunterhint.ui.registration

import ru.openunity.hunterhint.R

enum class Gender(val stringResourceId: Int) {
    MALE(R.string.male), FEMALE(R.string.female)
}

enum class Month(val days: Int, val stringResourceId: Int) {
    JANUARY(31, R.string.january), FEBRUARY(29, R.string.february), MARCH(
        31, R.string.march
    ),
    APRIL(30, R.string.april), MAY(31, R.string.may), JUNE(30, R.string.june), JULY(
        31, R.string.july
    ),
    AUGUST(31, R.string.august), SEPTEMBER(30, R.string.september), OCTOBER(
        31, R.string.october
    ),
    NOVEMBER(30, R.string.november), DECEMBER(31, R.string.december)
}

enum class Country(val countryName: String, val internationalCountryCode: String, val fIPS: String, val numberFormat: String) {
    JAMAICA("Jamaica","1876","JM","XXX XXXX"),
    SAINT_KITTS_NEVIS("Saint Kitts & Nevis","1869","KN","XXX XXXX"),
    TRINIDAD_TOBAGO("Trinidad & Tobago","1868","TT","XXX XXXX"),
    SAINT_VINCENT_THE_GRENADINES("Saint Vincent & the Grenadines","1784","VC","XXX XXXX"),
    DOMINICA("Dominica","1767","DM","XXX XXXX"),
    SAINT_LUCIA("Saint Lucia","1758","LC","XXX XXXX"),
    SINT_MAARTEN("Sint Maarten","1721","SX","XXX XXXX"),
    AMERICAN_SAMOA("American Samoa","1684","AS","XXX XXXX"),
    GUAM("Guam","1671","GU","XXX XXXX"),
    NORTHERN_MARIANA_ISLANDS("Northern Mariana Islands","1670","MP","XXX XXXX"),
    MONTSERRAT("Montserrat","1664","MS","XXX XXXX"),
    TURKS_CAICOS_ISLANDS("Turks & Caicos Islands","1649","TC","XXX XXXX"),
    GRENADA("Grenada","1473","GD","XXX XXXX"),
    BERMUDA("Bermuda","1441","BM","XXX XXXX"),
    CAYMAN_ISLANDS("Cayman Islands","1345","KY","XXX XXXX"),
    US_VIRGIN_ISLANDS("US Virgin Islands","1340","VI","XXX XXXX"),
    BRITISH_VIRGIN_ISLANDS("British Virgin Islands","1284","VG","XXX XXXX"),
    ANTIGUA_BARBUDA("Antigua & Barbuda","1268","AG","XXX XXXX"),
    ANGUILLA("Anguilla","1264","AI","XXX XXXX"),
    BARBADOS("Barbados","1246","BB","XXX XXXX"),
    BAHAMAS("Bahamas","1242","BS","XXX XXXX"),
    UZBEKISTAN("Uzbekistan","998","UZ","XX XXXXXXX"),
    KYRGYZSTAN("Kyrgyzstan","996","KG","XXX XXXXXX"),
    GEORGIA("Georgia","995","GE","XXX XXX XXX"),
    AZERBAIJAN("Azerbaijan","994","AZ","XX XXX XXXX"),
    TURKMENISTAN("Turkmenistan","993","TM","XX XXXXXX"),
    TAJIKISTAN("Tajikistan","992","TJ","XX XXX XXXX"),
    NEPAL("Nepal","977","NP","XX XXXX XXXX"),
    MONGOLIA("Mongolia","976","MN","XX XX XXXX"),
    BHUTAN("Bhutan","975","BT","XX XXX XXX"),
    QATAR("Qatar","974","QA","XX XXX XXX"),
    BAHRAIN("Bahrain","973","BH","XXXX XXXX"),
    ISRAEL("Israel","972","IL","XX XXX XXXX"),
    UNITED_ARAB_EMIRATES("United Arab Emirates","971","AE","XX XXX XXXX"),
    PALESTINE("Palestine","970","PS","XXX XX XXXX"),
    OMAN("Oman","968","OM","XXXX XXXX"),
    YEMEN("Yemen","967","YE","XXX XXX XXX"),
    SAUDI_ARABIA("Saudi Arabia","966","SA","XX XXX XXXX"),
    KUWAIT("Kuwait","965","KW","XXXX XXXX"),
    IRAQ("Iraq","964","IQ","XXX XXX XXXX"),
    SYRIA("Syria","963","SY","XXX XXX XXX"),
    JORDAN("Jordan","962","JO","X XXXX XXXX"),
    LEBANON("Lebanon","961","LB",""),
    MALDIVES("Maldives","960","MV","XXX XXXX"),
    TAIWAN("Taiwan","886","TW","XXX XXX XXX"),
    INTERNATIONAL_NETWORKS_883("International Networks","883","GO",""),
    INTERNATIONAL_NETWORKS_882("International Networks","882","GO",""),
    GLOBAL_MOBILE_SATELLITE("Global Mobile Satellite","881","GO",""),
    BANGLADESH("Bangladesh","880","BD",""),
    LAOS("Laos","856","LA","XX XX XXX XXX"),
    CAMBODIA("Cambodia","855","KH",""),
    MACAU("Macau","853","MO","XXXX XXXX"),
    HONG_KONG("Hong Kong","852","HK","X XXX XXXX"),
    NORTH_KOREA("North Korea","850","KP",""),
    MARSHALL_ISLANDS("Marshall Islands","692","MH",""),
    MICRONESIA("Micronesia","691","FM",""),
    TOKELAU("Tokelau","690","TK",""),
    FRENCH_POLYNESIA("French Polynesia","689","PF",""),
    TUVALU("Tuvalu","688","TV",""),
    NEW_CALEDONIA("New Caledonia","687","NC",""),
    KIRIBATI("Kiribati","686","KI",""),
    SAMOA("Samoa","685","WS",""),
    NIUE("Niue","683","NU",""),
    COOK_ISLANDS("Cook Islands","682","CK",""),
    WALLIS_FUTUNA("Wallis & Futuna","681","WF",""),
    PALAU("Palau","680","PW",""),
    FIJI("Fiji","679","FJ",""),
    VANUATU("Vanuatu","678","VU",""),
    SOLOMON_ISLANDS("Solomon Islands","677","SB",""),
    TONGA("Tonga","676","TO",""),
    PAPUA_NEW_GUINEA("Papua New Guinea","675","PG",""),
    NAURU("Nauru","674","NR",""),
    BRUNEI_DARUSSALAM("Brunei Darussalam","673","BN","XXX XXXX"),
    NORFOLK_ISLAND("Norfolk Island","672","NF",""),
    TIMOR_LESTE("Timor-Leste","670","TL",""),
    BONAIRE_SINT_EUSTATIUS_SABA("Bonaire, Sint Eustatius & Saba","599","BQ",""),
    CURACAO("Curaçao","599","CW",""),
    URUGUAY("Uruguay","598","UY","X XXX XXXX"),
    SURINAME("Suriname","597","SR","XXX XXXX"),
    MARTINIQUE("Martinique","596","MQ",""),
    PARAGUAY("Paraguay","595","PY","XXX XXX XXX"),
    FRENCH_GUIANA("French Guiana","594","GF",""),
    ECUADOR("Ecuador","593","EC","XX XXX XXXX"),
    GUYANA("Guyana","592","GY",""),
    BOLIVIA("Bolivia","591","BO","X XXX XXXX"),
    GUADELOUPE("Guadeloupe","590","GP","XXX XX XX XX"),
    HAITI("Haiti","509","HT",""),
    SAINT_PIERRE_MIQUELON("Saint Pierre & Miquelon","508","PM",""),
    PANAMA("Panama","507","PA","XXXX XXXX"),
    COSTA_RICA("Costa Rica","506","CR","XXXX XXXX"),
    NICARAGUA("Nicaragua","505","NI","XXXX XXXX"),
    HONDURAS("Honduras","504","HN","XXXX XXXX"),
    EL_SALVADOR("El Salvador","503","SV","XXXX XXXX"),
    GUATEMALA("Guatemala","502","GT","X XXX XXXX"),
    BELIZE("Belize","501","BZ",""),
    FALKLAND_ISLANDS("Falkland Islands","500","FK",""),
    LIECHTENSTEIN("Liechtenstein","423","LI",""),
    SLOVAKIA("Slovakia","421","SK","XXX XXX XXX"),
    CZECH_REPUBLIC("Czech Republic","420","CZ","XXX XXX XXX"),
    MACEDONIA("Macedonia","389","MK","XX XXX XXX"),
    BOSNIA_HERZEGOVINA("Bosnia & Herzegovina","387","BA","XX XXX XXX"),
    SLOVENIA("Slovenia","386","SI","XX XXX XXX"),
    CROATIA("Croatia","385","HR",""),
    KOSOVO("Kosovo","383","XK","XXXX XXXX"),
    MONTENEGRO("Montenegro","382","ME",""),
    SERBIA("Serbia","381","RS","XX XXX XXXX"),
    UKRAINE("Ukraine","380","UA","XX XXX XX XX"),
    SAN_MARINO("San Marino","378","SM","XXX XXX XXXX"),
    MONACO("Monaco","377","MC","XXXX XXXX"),
    ANDORRA("Andorra","376","AD","XX XX XX"),
    BELARUS("Belarus","375","BY","XX XXX XXXX"),
    ARMENIA("Armenia","374","AM","XX XXX XXX"),
    MOLDOVA("Moldova","373","MD","XX XXX XXX"),
    ESTONIA("Estonia","372","EE",""),
    LATVIA("Latvia","371","LV","XXX XXXXX"),
    LITHUANIA("Lithuania","370","LT","XXX XXXXX"),
    BULGARIA("Bulgaria","359","BG",""),
    FINLAND("Finland","358","FI",""),
    CYPRUS("Cyprus","357","CY","XXXX XXXX"),
    MALTA("Malta","356","MT","XX XX XX XX"),
    ALBANIA("Albania","355","AL","XX XXX XXXX"),
    ICELAND("Iceland","354","IS","XXX XXXX"),
    IRELAND("Ireland","353","IE","XX XXX XXXX"),
    LUXEMBOURG("Luxembourg","352","LU",""),
    PORTUGAL("Portugal","351","PT","X XXXX XXXX"),
    GIBRALTAR("Gibraltar","350","GI","XXXX XXXX"),
    GREENLAND("Greenland","299","GL","XXX XXX"),
    FAROE_ISLANDS("Faroe Islands","298","FO","XXX XXX"),
    ARUBA("Aruba","297","AW","XXX XXXX"),
    ERITREA("Eritrea","291","ER","X XXX XXX"),
    SAINT_HELENA_290("Saint Helena","290","SH","XX XXX"),
    COMOROS("Comoros","269","KM","XXX XXXX"),
    SWAZILAND("Swaziland","268","SZ","XXXX XXXX"),
    BOTSWANA("Botswana","267","BW","XX XXX XXX"),
    LESOTHO("Lesotho","266","LS","XX XXX XXX"),
    MALAWI("Malawi","265","MW","77 XXX XXXX"),
    NAMIBIA("Namibia","264","NA","XX XXX XXXX"),
    ZIMBABWE("Zimbabwe","263","ZW","XX XXX XXXX"),
    REUNION("Réunion","262","RE","XXX XXX XXX"),
    MADAGASCAR("Madagascar","261","MG","XX XX XXX XX"),
    ZAMBIA("Zambia","260","ZM","XX XXX XXXX"),
    MOZAMBIQUE("Mozambique","258","MZ","XX XXX XXXX"),
    BURUNDI("Burundi","257","BI","XX XX XXXX"),
    UGANDA("Uganda","256","UG","XX XXX XXXX"),
    TANZANIA("Tanzania","255","TZ","XX XXX XXXX"),
    KENYA("Kenya","254","KE","XXX XXX XXX"),
    DJIBOUTI("Djibouti","253","DJ","XX XX XX XX"),
    SOMALIA("Somalia","252","SO","XX XXX XXX"),
    ETHIOPIA("Ethiopia","251","ET","XX XXX XXXX"),
    RWANDA("Rwanda","250","RW","XXX XXX XXX"),
    SUDAN("Sudan","249","SD","XX XXX XXXX"),
    SEYCHELLES("Seychelles","248","SC","X XX XX XX"),
    SAINT_HELENA("Saint Helena","247","SH","XXXX"),
    DIEGO_GARCIA("Diego Garcia","246","IO","XXX XXXX"),
    GUINEA_BISSAU("Guinea-Bissau","245","GW","XXX XXXX"),
    ANGOLA("Angola","244","AO","XXX XXX XXX"),
    CONGO_DEM_REP("Congo (Dem. Rep.)","243","CD","XX XXX XXXX"),
    CONGO_REP("Congo (Rep.)","242","CG","XX XXX XXXX"),
    GABON("Gabon","241","GA","X XX XX XX"),
    EQUATORIAL_GUINEA("Equatorial Guinea","240","GQ","XXX XXX XXX"),
    SAO_TOME_PRINCIPE("São Tomé & Príncipe","239","ST","XX XXXXX"),
    CAPE_VERDE("Cape Verde","238","CV","XXX XXXX"),
    CAMEROON("Cameroon","237","CM","XXXX XXXX"),
    CENTRAL_AFRICAN_REP("Central African Rep.","236","CF","XX XX XX XX"),
    CHAD("Chad","235","TD","XX XX XX XX"),
    NIGERIA("Nigeria","234","NG",""),
    GHANA("Ghana","233","GH",""),
    SIERRA_LEONE("Sierra Leone","232","SL","XX XXX XXX"),
    LIBERIA("Liberia","231","LR",""),
    MAURITIUS("Mauritius","230","MU",""),
    BENIN("Benin","229","BJ","XX XXX XXX"),
    TOGO("Togo","228","TG","XX XXX XXX"),
    NIGER("Niger","227","NE","XX XX XX XX"),
    BURKINA_FASO("Burkina Faso","226","BF","XX XX XX XX"),
    COTE_DIVOIRE("Côte d`Ivoire","225","CI","XX XXX XXX"),
    GUINEA("Guinea","224","GN","XXX XXX XXX"),
    MALI("Mali","223","ML","XXXX XXXX"),
    MAURITANIA("Mauritania","222","MR","XXXX XXXX"),
    SENEGAL("Senegal","221","SN","XX XXX XXXX"),
    GAMBIA("Gambia","220","GM","XXX XXXX"),
    LIBYA("Libya","218","LY","XX XXX XXXX"),
    TUNISIA("Tunisia","216","TN","XX XXX XXX"),
    ALGERIA("Algeria","213","DZ","XXX XX XX XX"),
    MOROCCO("Morocco","212","MA","XX XXX XXXX"),
    SOUTH_SUDAN("South Sudan","211","SS","XX XXX XXXX"),
    IRAN("Iran","98","IR","XXX XXX XXXX"),
    MYANMAR("Myanmar","95","MM",""),
    SRI_LANKA("Sri Lanka","94","LK","XX XXX XXXX"),
    AFGHANISTAN("Afghanistan","93","AF","XXX XXX XXX"),
    PAKISTAN("Pakistan","92","PK","XXX XXX XXXX"),
    INDIA("India","91","IN","XXXXX XXXXX"),
    TURKEY("Turkey","90","TR","XXX XXX XXXX"),
    CHINA("China","86","CN","XXX XXXX XXXX"),
    VIETNAM("Vietnam","84","VN",""),
    SOUTH_KOREA("South Korea","82","KR",""),
    JAPAN("Japan","81","JP","XX XXXX XXXX"),
    THAILAND("Thailand","66","TH","X XXXX XXXX"),
    SINGAPORE("Singapore","65","SG","XXXX XXXX"),
    NEW_ZEALAND("New Zealand","64","NZ",""),
    PHILIPPINES("Philippines","63","PH","XXX XXX XXXX"),
    INDONESIA("Indonesia","62","ID",""),
    AUSTRALIA("Australia","61","AU","XXX XXX XXX"),
    MALAYSIA("Malaysia","60","MY",""),
    VENEZUELA("Venezuela","58","VE","XXX XXX XXXX"),
    COLOMBIA("Colombia","57","CO","XXX XXX XXXX"),
    CHILE("Chile","56","CL","X XXXX XXXX"),
    BRAZIL("Brazil","55","BR","XX XXXXX XXXX"),
    ARGENTINA("Argentina","54","AR",""),
    CUBA("Cuba","53","CU","XXXX XXXX"),
    MEXICO("Mexico","52","MX",""),
    PERU("Peru","51","PE","XXX XXX XXX"),
    GERMANY("Germany","49","DE",""),
    POLAND("Poland","48","PL","XXX XXX XXX"),
    NORWAY("Norway","47","NO","XXXX XXXX"),
    SWEDEN("Sweden","46","SE","XX XXX XXXX"),
    DENMARK("Denmark","45","DK","XXXX XXXX"),
    UNITED_KINGDOM("United Kingdom","44","GB","XXXX XXXXXX"),
    AUSTRIA("Austria","43","AT",""),
    Y_LAND("Y-land","42","YL",""),
    SWITZERLAND("Switzerland","41","CH","XX XXX XXXX"),
    ROMANIA("Romania","40","RO","XXX XXX XXX"),
    ITALY("Italy","39","IT",""),
    HUNGARY("Hungary","36","HU","XXX XXX XXX"),
    SPAIN("Spain","34","ES","XXX XXX XXX"),
    FRANCE("France","33","FR","X XX XX XX XX"),
    BELGIUM("Belgium","32","BE","XXX XX XX XX"),
    NETHERLANDS("Netherlands","31","NL","X XX XX XX XX"),
    GREECE("Greece","30","GR","XXX XXX XXXX"),
    SOUTH_AFRICA("South Africa","27","ZA","XX XXX XXXX"),
    EGYPT("Egypt","20","EG","XX XXXX XXXX"),
    KAZAKHSTAN("Kazakhstan","7","KZ","XXX XXX XX XX"),
    RUSSIAN_FEDERATION("Russian Federation","7","RU","XXX XXX XXXX"),
    PUERTO_RICO("Puerto Rico","1","PR","XXX XXX XXXX"),
    DOMINICAN_REP("Dominican Rep.","1","DO","XXX XXX XXXX"),
    CANADA("Canada","1","CA","XXX XXX XXXX"),
    USA("USA","1","US","XXX XXX XXXX"),
}