## Synopsis

This is Comment Filtering project. The main purpose is to detect Spam comments - which is advertising comments. These comments usually have common content about ads, use some special black-words and the variants of these black-words. And based on these features, the project define some rules to filter out these comments.

## Code Example
Simple API:

String result = id.co.babe.filter.CommentFilter.ruleInference(String input)
"0" - SPAM
"1" - NORMAL

## Motivation

The spam comments are written in a very special way, not like normal comments. And we can not using text-processing technique for this content because they are not written by normal words. Focusing on the differences between normal and spam comments, we define some features that can classify them out like:
- Upper-case letter
- Speical letter word
- Separate word into single character: Ex: V I A G R A
- Black word
- Contact word: Ex: pin, bbm, idline, phone-number
- Length of the comment

The spammers will continuously change the way they write spam comment to pass the filter-rules. And we will continuously update the rules, and hopefully we could find a general methods in future to detect these rules better.

## Tests

Sample code and evaluation in id.co.babe.filter.KomenFilterExample

## License

A short snippet describing the license (MIT, Apache, etc.)
