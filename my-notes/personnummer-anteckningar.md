# Personnummer
- hur många personer föds per dag i Sverige? ca 300
- personnummer är unika.
- 4 sista siffrorna:
    - 1-2: födelseplats, e.g. region/sjukhus. Också counter?
    - 3e: kön, udda för man, jämnt för kvinna. Också counter?
    - 4e: kontrollsiffra, deterministiskt beräknad med Luhn-algoritmen utifrån de 9 (eller 11?) första siffrorna i personnumret.

      # Specialfall:
        - tillfälliga personnummer, börjar ofta på "T"
            - immigranter: födelsenumret (XXX) väljs så att numret blir unikt bara, that's all.
            - immigranter som inte är folkboförda (?).
                - kort planerad vistelse (?). månaden blir (månad_x + 30). Detta kallas samordningsnummer.

# Query parameters
- "testpersonnummer"
- "_limit" + "_offset" ==> pagination
- _callback



