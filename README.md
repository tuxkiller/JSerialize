JSerialize
==========

An University of Zielona Góra student's project about serialization to JSON format in Java language.

0) TWORZENIE KLUCZA SSH WAZNE!!

Aby mieć dostęp do projektu należy wygenerować swój klucz SSH
-sciagamy program ze strony http://www.ecora.com/ecora/support/putty/puttygen-x86.exe
-wlaczamy program i klikamy przycisk generate (passphrase mozna zostawic puste)
-teraz nalezy poruszać kursorem w okienku programu aby stworzyć losowy klucz na podstawie ruchów
-po chwili zostanie wygenerowany klucz należy teraz wybrac u góry zakładkę Conversions i wybrac opcje export OpenSHH key
-plik nalezy nazwac "id_rsa" (bez rozszerzenia) i zapisac w folderze domowym w folderze .ssh (jezeli nie wiemy jaki to 

folder domowy nalezy wpisac w cmd polecenie "set HOME", jezeli nie ma w folderze domowym folderu .ssh nalezy go utworzyc)

WYGENEROWANY KLUCZ PRZESLAC DO KOORDYNATORA NA FB W WIADOMOSCI PRYWATNEJ INACZEJ NIE BEDZIE DOSTEPU


1) Instalacja narzędzia do obsługi systemu kontroli wersji - git

(WINDOWS)
- pobranie ze strony http://git-scm.com/download
- rozpoczęcie instalacji
- akceptacja licencji
- pozostawienie ustawień standardowych
- wybranie opcji Run git from the Windwos Command Prompt
- dokonczenie instalacji

(LINUX)
- zależnie od dystrybucji mogłoby to być (apt-get install git) dla Ubuntu lub (aptitude install git) dla Debiana lub inne
- możliwe także pobranie paczki ze strony http://git-scm.com/download
- użycie programu dpkg do instalacji


2) Podstawowe pojęcia dla git

- git clone xxx -- gdzie xxx to adres repozytorium, powoduje pobranie projektu na dysk lokalny
- git status -- polecenie sprawdza czy nastapily jakies zmiany LOKALNE jezeli tak zostanie wystosowany odpowiedni komunikat
- git add xxx-- jezeli polecenie git status wykazuje zmiany, poleceniem tym gdzie xxx to nazwa pliku zalaczamy zmienione 

pliki do pozniejszego wykonania commitu
- git commit -- zapisanie aktualnego stanu plikow LOKALNIE i nadanie mu unikalnej nazwy hashowanej oraz opatrzenie zmian 

odpowiednim komentarzem np. git commit -m "Pierwsza zmiana", polecenie git commit -a -m pozwala git'owi automatycznie 

wykonac polecenie git add na wszystkich zmienionych plikach
- git checkout -- przemieszczanie się pomiędzy różnymi wersjami (stanami) projektu, np powrot do poprzedniego commitu, bądź 

innego brancha
- git branch -- polecenie sprawdza czy projekt zawiera inne branche niz standardowy master (branch można rozumieć jako 

osobną scieżkę rozwoju projektu która jednak posiada wspólny punkt (stan) z innymi ścieżkami (branchami)). aby utworzyć 

osobny branch należy użyć polecenie git branch xxx gdzie xxx to nazwa
- git fetch -- pobranie ewentulanych modyfikacji z serwera zdalnego dla naszego projektu
- git push -- zapisanie zmian na serwerze zdalnym (nikt nie pushuje bez wiedzy koordynatora)
- git push xxx -- zapisanie na serwerze oddzielnego brancha

3) Używanie

Większość operacji jakie wykonujemy, wykonujemy jedynie na plikach lokalnych. Do zapisywanie zmian na serwerze zdalnym 
sluży komenda git push. Wtedy ma miejsce aktualizacja plików zdalnych na podstawie naszych lokalnych zmian. BARDZO WAŻNE 
jest aby tuż przed wywołaniem komendy git push wywołać komendę git fetch. Inaczej jeżeli ktoś inny zapisał jakies zmiany na 
serwerze mogą one zostać przez nas nadpisane.

4) Jak będzie wyglądać praca z narzędziem git?

Wszystkie komendy wykonujemy za pomocą lini poleceń znajdując się przy tym w folderze z projektem np:
C:\JSerialize\

USTAWIAMY GLOBALNA KONFIGURACJE
- git config --global user.name "IMIE NAZWISKO"
- git config --global user.email EMAIL

Należy pobrać ze zdalnego repozytorium nasz projekt. Otwieramy konsole/terminal i wykonujemy polecenie:

- git clone git@github.com:exesoft/JSerialize.git

Polecenie to utworzy w lokalizacji, której się znajdujemy folder o nazwie JSerialize

Po pobraniu powiedzmy, że chcemy wprowadzić zmiany w plikach, a więc otwieramy plik za pomocą ulubionego edytora i 

modyfikujemy zawartość (załóżmy ze zmiany w pliku o nazwe plik.txt), po czym wykonujemy komendy

- git add plik.txt
- git commit -m "Komentarz"
- git fetch
- ****git push***** -- tylko przed uprzednim otrzymaniem zgody koordynatora, chyba, ze pracujemy na osobnym od innych 
branchu

5 CZERWCA ODDANIE






