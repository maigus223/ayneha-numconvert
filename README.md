# AYNEHA NumConvert - Application Android (Kotlin + Jetpack Compose) - version 1.3.0

Application native Android reproduisant le convertisseur de chiffres entre six
systèmes : AYNEHA, N'ko, Adlam, Dogon NÈNÈ, Arabe et Français, avec le même thème (noir profond / or / cyan) que la
version web, et une icône « i » sur l'accueil qui ouvre la page Confidentialité.

## Ouvrir le projet

1. Installez **Android Studio** (dernière version stable - "Koala" ou plus récent).
2. `Fichier > Ouvrir` puis sélectionnez le dossier `AynehaConverterApp`.
3. Android Studio propose d'installer le wrapper Gradle manquant
   (`gradle-wrapper.jar`) automatiquement à la première ouverture - acceptez.
   Si ce n'est pas proposé, exécutez une fois, à la racine du projet :
   `gradle wrapper --gradle-version 8.7` (nécessite Gradle installé localement).
4. Laissez la synchronisation Gradle se terminer (elle télécharge les
   dépendances Jetpack Compose - connexion Internet requise **uniquement
   pour compiler**, pas pour l'usage de l'application une fois installée).
5. Lancez sur un émulateur ou un téléphone via le bouton ▶ (Run).

## Ce qui est déjà en place

- **Écran d'accueil** : en-tête (« AYNEHA NumConvert » + nom en écriture
  AYNEHA), icône « i » en haut à droite, sélecteur de langue, pavé numérique
  adaptatif (miroir RTL pour AYNEHA/N'ko/Adlam/Dogon NÈNÈ, LTR pour l'Arabe et le Français), et les
  6 plaques de résultat converties en direct, séparateur de milliers inclus.
- **Écran Confidentialité** : accessible via l'icône « i », texte rédigé pour
  cette application précise, terminé par la mention demandée
  (`Développé par MAIGUS : gwokmt2q@duck.com`).
- **Police AYNEHA** : embarquée directement dans l'app (`res/font/ayneha_pro.ttf`, police de transition qui contient l'ancienne plage et le bloc plan 16),
  aucune connexion requise pour l'afficher.
- **Polices Arabe / N'ko / titre** : via le système de polices téléchargeables
  de Google (Jetpack Compose + Google Play Services) - se téléchargent une
  seule fois en arrière-plan, puis restent en cache sur l'appareil. Aucune
  permission Internet n'est déclarée dans l'application elle-même.

## À compléter vous-même

- **Icône de l'application** : aucune icône personnalisée n'est fournie.
  Dans Android Studio : clic droit sur `res` > `New > Image Asset`, pour
  générer une icône à partir d'un logo AYNEHA.
- **Signature / publication** : pour publier sur le Google Play Store, il
  faudra générer un keystore de signature (`Build > Generate Signed App
  Bundle / APK` dans Android Studio) - ce projet fournit uniquement la
  version de développement (debug).
- **Vérification des certificats Google Fonts** (`res/values/font_certs.xml`) :
  ce sont les certificats standard documentés par Android pour les polices
  téléchargeables. S'ils ont changé depuis la rédaction de ce projet, les
  polices Arabe/N'ko/titre basculeront simplement sur une police système de
  repli (aucun crash) - comparez au besoin avec la documentation officielle :
  https://developer.android.com/develop/ui/compose/text/fonts#downloadable-fonts

## Structure du code

```
app/src/main/java/com/maigus/ayneha/converter/
├── MainActivity.kt              point d'entrée
├── navigation/AppNav.kt         accueil ⇄ confidentialité
├── data/DigitMaps.kt            chiffres, ordre du clavier, séparateurs
├── ui/theme/                    couleurs, polices, thème Material3
├── ui/components/               LangPicker, Keypad, ResultPlate
└── ui/screens/                  HomeScreen, PrivacyScreen
```

## Version 1.3.0

- Ajout des chiffres **Adlam** (écriture du peul, U+1E950 - U+1E959) : sixième système,
  dans le sélecteur, le pavé numérique et les résultats. Comme le N'ko, un nombre adlam se lit
  de droite à gauche. Police embarquée `noto_sans_adlam.ttf` (Noto Sans Adlam, SIL OFL 1.1,
  fichier `OFL-NotoSansAdlam.txt`) : aucune connexion nécessaire pour l'afficher.
- Ordre commun du sélecteur et des résultats : AYNEHA, N'ko, Adlam, Dogon NÈNÈ, Arabe, Français.
- En-tête « AYNEHA barmey-ko » redessiné en image nette (haute résolution).
- Nouveau logo à six branches (une par système de chiffres).
- `versionCode` 3 / `versionName` 1.3.0.

## Version 1.2.0

- Migration des chiffres AYNEHA vers le bloc plan 16 (U+10FF7C - U+10FF85), avec la
  police de transition `ayneha_pro.ttf` (remplace `ayneha_type.ttf`). Les chiffres sont des
  paires de substitution UTF-16 et l'inversion du texte se fait par points de code.
- Les chiffres Dogon NÈNÈ gardent leur police `nene_regular.ttf` et leur plage actuelle.
- La page Confidentialité affiche maintenant la version et la date de mise à jour.
- Kit de licences commun aux applications AYNEHA.
- `versionCode` 2 / `versionName` 1.2.0.

## Licences

- **Code** : GNU GPL v3 ou ultérieure (fichier `LICENSE`), en-têtes SPDX
  `GPL-3.0-or-later`. Copyright (C) 2026 MAIGUS.
- **Police AYNEHA** : SIL Open Font License 1.1 (fichier `OFL.txt`), nom réservé « AYNEHA ».
- **Police Noto Sans Adlam** : SIL Open Font License 1.1 (fichier `OFL-NotoSansAdlam.txt`), The Noto Project Authors.
- **Logos et images** : CC BY-SA 4.0. Le nom et le logo AYNEHA sont réservés.
- Développé par MAIGUS avec l'aide d'outils d'IA (Claude, Anthropic).

Logiciel libre (GPL v3). Code source : github.com/maigus223/ayneha-numconvert

Contact : gwokmt2q@duck.com
