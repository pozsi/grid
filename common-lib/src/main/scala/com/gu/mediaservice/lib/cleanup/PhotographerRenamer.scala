package com.gu.mediaservice.lib.cleanup

import com.gu.mediaservice.model.ImageMetadata

object PhotographerRenamer extends MetadataCleaner {
  val names = Map(
    "Adam Warzawa" -> "Adam Warżawa",
    "Akos Stiller" -> "Ákos Stiller",
    "Aleksander Kozminski" -> "Aleksander Koźmiński",
    "Alberto E. Rodriguez" -> "Alberto E Rodríguez",
    "Alecsandra Raluca Dragoi" -> "Alecsandra Raluca Drăgoi",
    "Ali Ihsan Ozturk" -> "Ali İhsan Öztürk",
    "Alik Keplicz" -> "Alik Kęplicz",
    "Alvaro Barrientos" -> "Álvaro Barrientos",
    "Amy T. Zielinski" -> "Amy T Zielinski",
    "Andreea Campeanu" -> "Andreea Câmpeanu",
    "Andrej Isakovic" -> "Andrej Isaković",
    "Andres Kudacki" -> "Andrés Kudacki",
    "Antonio Bronic" -> "Antonio Bronić",
    "Antonio Lacerda" -> "António Lacerda",
    "Attila Kovacs" -> "Attila Kovács",
    "Arif Hudaverdi Yaman" -> "Arif Hüdaverdi Yaman",
    "Balazs Mohai" -> "Balázs Mohai",
    "Bartlomiej Zborowski" -> "Bartłomiej Zborowski",
    "Berk Ozkan" -> "Berk Özkan",
    "Bernadett Szabo" -> "Bernadett Szabó",
    "Bilgin S. Sasmaz" -> "Bilgin S. Şaşmaz",
    "Bjorn Larsson Rosvall" -> "Björn Larsson Rosvall",
    "Boris Kovacev" -> "Boris Kovačev",
    "Bulent Kilic" -> "Bülent Kılıç",
    "Carlos Alvarez" -> "Carlos Álvarez",
    "Carlos Garcia Rawlins" -> "Carlos García Rawlins",
    "Cathal Mcnaughton" -> "Cathal McNaughton",
    "Czarek Sokolowski" -> "Czarek Sokołowski",
    "Cem Oksuz" -> "Cem Öksüz",
    "Cristobal Venegas" -> "Cristóbal Venegas",
    "Damir Sagolj" -> "Damir Šagolj",
    "Daniel Mihailescu" -> "Daniel Mihăilescu",
    "Darko Bandic" -> "Darko Bandić",
    "Darko Vojinovic" -> "Darko Vojinović",
    "David M. Benett" -> "David M Benett",
    "Dawid Zuchowicz" -> "Dawid Żuchowicz",
    "Diana Sanchez" -> "Diana Sánchez",
    "Eamonn M. McCormack" -> "Eamonn M McCormack",
    "Eduardo Munoz" -> "Eduardo Muñoz",
    "Elif Ozturk" -> "Elif Öztürk",
    "Erdem Sahin" -> "Erdem Şahin",
    "Esteban Felix" -> "Esteban Félix",
    "Francois Lenoir" -> "François Lenoir",
    "Francois Mori" -> "François Mori",
    "Francois-Xavier Marit" -> "François-Xavier Marit",
    "Gary A. Vasquez" -> "Gary A Vasquez",
    "Gerard Julien" -> "Gérard Julien",
    "Gergely Janossy" -> "Gergely Jánossy",
    "Goran Kovacic" -> "Goran Kovačić",
    "Goran Tomasevic" -> "Goran Tomašević",
    "Ints Kalnins" -> "Ints Kalniņš",
    "Isa Terli" -> "İsa Terli",
    "Isaiah J. Downing" -> "Isaiah J Downing",
    "Jakub Kaminski" -> "Jakub Kamiński",
    "James D. Morgan" -> "James D Morgan",
    "Jean-Francois Badias" -> "Jean-François Badias",
    "Jean-Paul Pelissier" -> "Jean-Paul Pélissier",
    "Jerome Favre" -> "Jérôme Favre",
    "John E. Sokolowski" -> "John E Sokolowski",
    "John Vizcaino" -> "John Vizcaíno",
    "Jorg Carstensen" -> "Jörg Carstensen",
    "Jose Fragozo" -> "José Fragozo",
    "Jose Giribas" -> "José Giribás",
    "José Luis González" -> "José Luis González",
    "Jose Palazon" -> "José Palazón",
    "Jose Luis Magana" -> "José Luis Magaña",
    "Jose Sena Goulao" -> "José Sena Goulão",
    "Juan Naharro Gimenez" -> "Juan Naharro Giménez",
    "Kai Schwoerer" -> "Kai Schwörer",
    "Laszlo Balogh" -> "László Balogh",
    "Lech Muszynski" -> "Lech Muszyński",
    "Lise Aserud" -> "Lise Åserud",
    "Lucio Tavora" -> "Lúcio Távora",
    "Lukasz Cynalewski" -> "Łukasz Cynalewski",
    "Lukasz Szelemej" -> "Łukasz Szełemej",
    "M.Stan Reaves" -> "M Stan Reaves",
    "Maciej Kulczynski" -> "Maciej Kulczyński",
    "Maciek Jazwiecki" -> "Maciek Jaźwiecki",
    "Mahmut Serdar Alakus" -> "Mahmut Serdar Alakuş",
    "Marcio Jose Sanchez" -> "Marcio José Sánchez",
    "Mario Arturo Martinez" -> "Mario Arturo Martínez",
    "Mario Vazquez" -> "Mario Vázquez",
    "Marko Djurica" -> "Marko Đurica",
    "Matthias Schrader" -> "Matthias Schräder",
    "Mauricio Duenas Castaneda" -> "Mauricio Dueñas Castañeda",
    "Michal Cizek" -> "Michal Čížek",
    "Milos Bicanski" -> "Miloš Bičanski",
    "Mustafa Ciftci" -> "Mustafa Çiftçi",
    "Nam Y. Huh" -> "Nam Y Huh",
    "Oscar del Pozo" -> "Óscar del Pozo",
    "Ozan Kose" -> "Ozan Köse",
    "Ozge Elif Kizil" -> "Özge Elif Kızıl",
    "Ozkan Bilgin" -> "Özkan Bilgin",
    "Pablo Blazquez Dominguez" -> "Pablo Blázquez Domínguez",
    "Pablo Martinez Monsivais" -> "Pablo Martínez Monsiváis",
    "Pal Hansen" -> "Pål Hansen",
    "Rade Prelic" -> "Rade Prelić",
    "Rafal Guz" -> "Rafał Guz",
    "Raul Caro Cadenas" -> "Raúl Caro Cadenas",
    "Reinnier Kaze" -> "Reinnier Kazé",
    "Remy Gabalda" -> "Rémy Gabalda",
    "Ricardo Mazalan" -> "Ricardo Mazalán",
    "Sanjin Strukic" -> "Sanjin Strukić",
    "Sebastian Tataru" -> "Sebastian Tătaru",
    "Sebastien Bozon" -> "Sébastien Bozon",
    "Sebastien Nogier" -> "Sébastien Nogier",
    "Sergio Perez" -> "Sergio Pérez",
    "Stanislaw Rozpedzik" -> "Stanisław Rozpędzik",
    "Stephane de Sakutin" -> "Stéphane de Sakutin",
    "Stephane Mahe" -> "Stéphane Mahé",
    "Szilard Koszticsak" -> "Szilárd Koszticsák",
    "Thearon W. Henderson" -> "Thearon W Henderson",
    "Tim P. Whitby" -> "Tim P Whitby",
    "Tamás Kaszás" -> "Tamás Kaszás",
    "Tamas Kovacs" -> "Tamás Kovács",
    "Tytus Zmijewski" -> "Tytus Żmijewski",
    "Vadim Ghirda" -> "Vadim Ghirdă",
    "Valerie Gache" -> "Valérie Gache",
    "Valery Hache" -> "Valéry Hache",
    "Vit Simanek" -> "Vít Šimánek",
    "Vladimir Simicek" -> "Vladimír Šimíček",
    "Umit Bektas" -> "Ümit Bektaş",
    "Wiktor Dabkowski" -> "Wiktor Dąbkowski",
    "Wojciech Kruczynski" -> "Wojciech Kruczyński",
    "Wojtek Radwanski" -> "Wojtek Radwański",
    "Yasin Akgul" -> "Yasin Akgül",
    "Zoltan Gergely Kelemen" -> "Zoltán Gergely Kelemen",
    "Zvonimir Barisin" -> "Zvonimir Barišin"
  )

  override def clean(metadata: ImageMetadata): ImageMetadata = {
    metadata.copy(byline = metadata.byline.flatMap(names.get))
  }
}
