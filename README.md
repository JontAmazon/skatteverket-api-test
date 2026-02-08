# Mini Test Suite for Skatteverket's Test Personal Number API
Note about [the API](https://www7.skatteverket.se/portal/apier-och-oppna-data/utvecklarportalen/oppetdata/Test%C2%AD%C2%ADpersonnummer):
It provides test personal numbers for developers; at least 1 test personal number per date of birth (from 1890 until 2025). 
Usually 2 per date of birth (one male, one female), but sometimes only 1, IINM.

## Requirements
- JDK 17

## Setup
Run this Maven Wrapper script. It downloads and runs Maven version 3.9.9, and runs all tests.
```bash
mvnw test      # Unix/macOS (untested)
mvnw.cmd test  # Windows
```

## Assumptions

### Assumptions about API Usage
I'm guessing the most common use case of the API is that a developer needs a virtual person object 
with a specific date of birth. Thus, I thought this was the most critical feature of the API, 
motivating my choice of happy case test scenario.



## Implemented Tests + What They Verify
- TODO later, when I say.



# happy case - what does it verify
- also _limit?




