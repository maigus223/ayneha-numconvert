# AYNEHA NumConvert - Application Android (Kotlin + Jetpack Compose)

Application native Android reproduisant le convertisseur de chiffres AYNEHA ⇄
Français ⇄ Arabe ⇄ N'ko, avec le même thème (noir profond / or / cyan) que la
version web, et une icône « i » sur l'accueil qui ouvre la page Confidentialité.

## Ouvrir le projet

1. Installez **Android Studio** (dernière version stable — "Koala" ou plus récent).
2. `Fichier > Ouvrir` puis sélectionnez le dossier `AynehaConverterApp`.
3. Android Studio propose d'installer le wrapper Gradle manquant
   (`gradle-wrapper.jar`) automatiquement à la première ouverture — acceptez.
   Si ce n'est pas proposé, exécutez une fois, à la racine du projet :
   `gradle wrapper --gradle-version 8.7` (nécessite Gradle installé localement).
4. Laissez la synchronisation Gradle se terminer (elle télécharge les
   dépendances Jetpack Compose — connexion Internet requise **uniquement
   pour compiler**, pas pour l'usage de l'application une fois installée).
5. Lancez sur un émulateur ou un téléphone via le bouton ▶ (Run).

## Ce qui est déjà en place

- **Écran d'accueil** : en-tête (« AYNEHA NumConvert » + nom en écriture
  AYNEHA), icône « i » en haut à droite, sélecteur de langue, pavé numérique
  adaptatif (miroir RTL pour AYNEHA/N'ko/Arabe, LTR pour le Français), et les
  4 plaques de résultat converties en direct, séparateur de milliers inclus.
- **Écran Confidentialité** : accessible via l'icône « i », texte rédigé pour
  cette application précise, terminé par la mention demandée
  (`Développé par MAIGUS : gwokmt2q@duck.com`).
- **Police AYNEHA** : embarquée directement dans l'app (`res/font/ayneha_type.ttf`),
  aucune connexion requise pour l'afficher.
- **Polices Arabe / N'ko / titre** : via le système de polices téléchargeables
  de Google (Jetpack Compose + Google Play Services) — se téléchargent une
  seule fois en arrière-plan, puis restent en cache sur l'appareil. Aucune
  permission Internet n'est déclarée dans l'application elle-même.

## À compléter vous-même

- **Icône de l'application** : aucune icône personnalisée n'est fournie.
  Dans Android Studio : clic droit sur `res` > `New > Image Asset`, pour
  générer une icône à partir d'un logo AYNEHA.
- **Signature / publication** : pour publier sur le Google Play Store, il
  faudra générer un keystore de signature (`Build > Generate Signed App
  Bundle / APK` dans Android Studio) — ce projet fournit uniquement la
  version de développement (debug).
- **Vérification des certificats Google Fonts** (`res/values/font_certs.xml`) :
  ce sont les certificats standard documentés par Android pour les polices
  téléchargeables. S'ils ont changé depuis la rédaction de ce projet, les
  polices Arabe/N'ko/titre basculeront simplement sur une police système de
  repli (aucun crash) — comparez au besoin avec la documentation officielle :
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
