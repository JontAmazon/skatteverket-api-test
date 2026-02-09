# Mini Test Suite for Skatteverket's API for Test-Personal Numbers
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

## Assumptions about API Usage
I'm guessing the most common use case of the API is that a developer needs a virtual person object 
with a specific date of birth. Thus, I thought this was the most critical feature of the API, 
motivating my choice of happy test case.

## Implemented Happy Test Case
**filtersByBirthdate_returnsResults:**

Filter by testpersonnummer to match a specific date of birth (constant, always the same). Assert at least one match.

What we verify:
- the API returns 200
- the API returns results (test personal numbers)
- the filtering functionality works
- the response body is valid json


## Implemented Negative Test Case
**limitAboveMaxCapsToMax:**

Given a huge input value for _limit, verify the API caps it to the default/max cap value.

