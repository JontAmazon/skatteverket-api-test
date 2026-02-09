# ❤️🧡💛💚💙💜🤎🖤❤️🧡💛💚💙💜🤎🖤❤️🧡💛💚💙💜🤎🖤

# Happy case - other candidates/"honorable mentions":
- addition to chosen happy case:
  - assert less than e.g. 10 result matches? hm..
- plain fetch
- filtering still works no matter what values _limit and _offset have?
- fetch without any params, then fetch with filter/filters; verify less results?
- _limit=10 --> verify exactly 10 hits?
- use all 3 "main params" at once?
    - (could be nice to involve more logic, so the functionality is a bit more likely to break).

# Negative case - other candidates/"honorable mentions":
_limit = 0?
- Nonsense filter returns empty result set
    ?testpersonnummer=NOT_A_PNUMMER*&_limit=5
      (undefined params ignored or 4xx?)
- offset too large?
- _callback?
    - maybe not so important, but supported, could break, and easy to test.
    - negative test: _callback=(   expect 4xx?
    - positive test: _callback=cb  expect 200, response body = cb(<json>), inner body parses as valid json
- fetch a REAL personal number, or a fake one with a faulty last digit (Luhn alg.)
    - nah! I trust my happy case test. So this would rather test the dataset, and that's not necessary.

# More Test Ideas
- param edge cases:
	- _limit: expects [1, 500].
	- _offset too large
- ~~bad param values: negative numbers?~~




