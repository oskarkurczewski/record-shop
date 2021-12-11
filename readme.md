# MODEL

* Aplikacja ma odwzorowywać prosty model biznesowy, w którym użytkownicy końcowi aplikacji (dalej zwani Klientami) mogą dokonywać wyłącznej alokacji pewnych Zasobów definiowanych przez aplikację, w określonych przedziałach czasu. Do tej klasy modeli biznesowych należą wszelkiego rodzaju wypożyczalnie, hotele, umawianie spotkań etc.

* Model danych aplikacji powinien obejmować co najmniej trzy klasy:

* w każdej z klas / hierarchii powinien wystąpić atrybut, którego wartość będzie wymagana i unikatowa dla całego zbioru obiektów danej hierarchii oraz będzie podstawą relacji równości tych obiektów, a jednocześnie jako atrybut nie będzie miał znaczenia w domenie (tzw. identyfikator/klucz); może to być wartość całkowita lub UUID
hierarchia klas reprezentująca Użytkowników aplikacji, uwzględniająca podział na co najmniej trzy poziomy dostępu do aplikacji (poziom administratora użytkowników, poziom zarządcy zasobów, poziom użytkownika zasobów - Klienta)
    * w modelu Użytkowników musi występować atrybut, którego wartość określa, czy dany Użytkownik jest aktywny tj. może wykonywać akcje w aplikacji
    * w modelu Użytkowników musi występować atrybut typu tekstowego będący identyfikatorem (tzw. login), którego wartość będzie wymagana i unikatowa dla całego zbioru obiektów (niezależnie od istnienia klucza w tych obiektach)
* Klasa reprezentująca Zasób aplikacji; nie jest wymagane reprezentowanie wielu typów zasobów
* klasa reprezentująca fakt alokacji Zasobu przez Klienta, której minimalnym zbiorem atrybutów są: Klient, Zasób, czas rozpoczęcia alokacji, czas zakończenia alokacji;

# FUNKCJONALNOŚĆ

Funkcjonalność aplikacji powinna obejmować:


* wyszukiwanie wszystkich rodzajów obiektów według wartości klucza (wynikiem powinien być jeden obiekt)
* wyszukiwanie Użytkowników także według wartości identyfikatora tekstowego w dwóch wariantach: wyszukiwanie dokładnie podanej wartości (wynikiem powinien być jeden obiekt) oraz dopasowanie podanej wartości (wynikiem powinna być lista obiektów)
* zestaw operacji CRUD (tworzenie, odczyt (lista), modyfikacja, usuwanie) dla hierarchii Zasobów, przy czym usunięcie Zasobu jest możliwe tylko wtedy, gdy nie jest z nim związana żadna alokacja
* zestaw operacji CRU (tworzenie, odczyt (lista), modyfikacja) dla hierarchii Użytkowników, dodatkowo jako odrębne operacje należy uwzględnić aktywowanie i deaktywowanie Użytkownika
* utworzenie alokacji dla wskazanego Klienta oraz Zasobu (wskazanie poprzez wartość klucza), obwarowane co najmniej aktywnością Klienta oraz dostępnością (brakiem nie zakończonych alokacji) Zasobu
* pobranie (odrębnie) listy alokacji minionych oraz bieżących dla wskazanego Klienta lub Zasobu
* zakończenie alokacji i usuwanie alokacji
    * czas rozpoczęcia tworzonej alokacji może być ustawiany jako przyszły
    * zakończenie alokacji polega na ustawieniu atrybutu czasu zakończenia alokacji
    * usuwanie alokacji dotyczy tylko alokacji nie zakończonych
* operacje na alokacjach są prowadzone z perspektywy osoby trzeciej (operatora aplikacji)

UWAGA: Nie ma potrzeby i nie jest zalecane rozbudowywanie funkcjonalności ponad wyżej wymienione wymagania
WSKAZÓWKA: Dobrą bazą do budowy modelu aplikacji powinien być projekt realizowany w ramach przedmiotu Programowanie Obiektowe
UWAGA: Model aplikacji nie może powielać modelu "Wypożyczalnia pojazdów" używanego jako referencyjny


# WARUNKI POPRAWNOŚCI

W implementacji aplikacji należy uwzględnić sprawdzanie poprawności danych wejściowych, a w szczególności:

* walidację składniową danych przesyłanych w żądaniu, przy czym niespełnianie warunków poprawności powinno powodować uznanie żądania za niepoprawne
* weryfikację możliwości realizacji operacji ze względu na stan danych aplikacji (zgodnie z opisem funkcjonalności)

# SZCZEGÓŁY ARCHITEKTONICZNE

Aplikacja powinna być zrealizowana w modelu warstwowym, a w szczególności powinny być wyodrębnione dwie warstwy:

* warstwa tzw. menedżerów - obiektów wykonawczych udostępniających realizację wszystkich funkcji warstwy logiki; funkcjonalność menedżerów jest dostępna jako Rest API
* warstwa tzw. źródeł danych (DAO, repozytoria) - obiektów wykonawczych realizujących funkcjonalność składowania danych (z uwzględnieniem unikatowości identyfikatorów biznesowych)
    * składowanie danych powinno być realizowane w pamięci aplikacji, przy czym konstrukcja aplikacji powinna uwzględniać możliwość łatwej (nie wymagającej zmiany kodu klas menedżerów) zmiany implementacji źródeł danych
    * uruchamianie aplikacji powinno wiązać się z wczytaniem zestawu danych inicjujących
    * źródło danych jest odpowiedzialne za spełnianie warunku unikalności i niepustości kluczy oraz identyfikatora tekstowego (optymalność wykonania nie podlega ocenie)
    * źródło danych jest odpowiedzialne za nadawanie wartości kluczy nowo tworzonym obiektom
* poszczególne hierarchie obiektów aplikacji powinny być obsługiwane przez odrębne klasy menedżerów / źródeł danych (zasada pojedynczej odpowiedzialności)
* operacje zawężania zbioru danych (filtrowanie, wyszukiwanie) powinny być realizowane przez źródła danych (zasada minimalizacji transferu danych)

#OBSŁUGA WIELOWĄTKOWOŚCI

Należy pamiętać, że aplikacja będzie wykonywana wielowątkowo na poziomie obsługi żądań.

* obiekty źródeł danych muszą być współdzielone pomiędzy poszczególne żądania aplikacji Rest
    * można i należy zastosować dostępne mechanizmy synchronizacji (np. modyfikator synchronized w języku Java), aby wykluczyć współbieżne operacje zmieniające stan źródła danych (rzeczywiste rozwiązanie problemu wykracza poza zakres przedmiotu)
* w przypadku pozostałych klas wykonawczych należy unikać współdzielenia tych obiektów pomiędzy wątkami (stosować odpowiednie mechanizmy kontenera / frameworku aplikacyjnego)

#CECHY SPECYFICZNE APLIKACJI REST

Należy uwzględnić cechy specyficzne dla aplikacji Rest, wynikające z komunikacji w modelu żądanie - odpowiedź, w szczególności:

* ryzyko hazardu związanego z przeplotem żądań prowadzących do modyfikacji stanu źródeł danych
* możliwość ponownego przesłania identycznego żądania