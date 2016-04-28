
lexer grammar MySQLLexer;
@ header { 
}

SELECT
: 'select' | 'SELECT'
;


FROM
: 'from' | 'FROM'
;


WHERE
: 'where' | 'WHERE'
;


AND
: 'and' | '&&' | 'AND'
;


OR
: 'or' | '||' | 'OR'
;


XOR
: 'xor' | 'XOR'
;


IS
: 'is' | 'IS'
;


NULL
: 'null' | 'NULL'
;


LIKE
: 'like' | 'LIKE'
;


IN
: 'in' | 'IN'
;


EXISTS
: 'exists' | 'EXISTS'
;


ALL
: 'all' | 'ALL'
;


ANY
: 'any' | 'ANY'
;


TRUE
: 'true' | 'TRUE'
;


FALSE
: 'false' | 'FALSE'
;


DIVIDE
: 'div' | '/' | 'DIV'
;


MOD
: 'mod' | '%'
;


BETWEEN
: 'between' | 'BETWEEN'
;


REGEXP
: 'regexp'
;


PLUS
: '+'
;


MINUS
: '-'
;


NEGATION
: '~'
;


VERTBAR
: '|'
;


BITAND
: '&'
;


POWER_OP
: '^'
;


BINARY
: 'binary'
;


SHIFT_LEFT
: '<<'
;


SHIFT_RIGHT
: '>>'
;


ESCAPE
: 'escape'
;


ASTERISK
: '*'
;


RPAREN
: ')'
;


LPAREN
: '('
;


RBRACK
: ']'
;


LBRACK
: '['
;


COLON
: ':'
;


ALL_FIELDS
: '.*'
;


EQ
: '='
;


LTH
: '<'
;


GTH
: '>'
;


NOT_EQ
: '!='
;


NOT
: 'not' | 'NOT'
;


LET
: '<='
;


GET
: '>='
;


SEMI
: ';'
;


COMMA
: ','
;


DOT
: '.'
;


COLLATE
: 'collate' | 'COLLATE'
;


INNER
: 'inner' | 'INNER'
;


OUTER
: 'outer' | 'OUTER'
;


JOIN
: 'join' | 'JOIN'
;


CROSS
: 'cross' | 'CROSS'
;


USING
: 'using' | 'USING'
;


INDEX
: 'index' | 'INDEX'
;


KEY
: 'key' | 'KEY'
;


ORDER
: 'order' | 'ORDER'
;


GROUP
: 'group' | 'GROUP'
;


BY
: 'by' | 'BY'
;


FOR
: 'for' | 'FOR'
;


USE
: 'use' | 'USE'
;


IGNORE
: 'ignore' | 'IGNORE'
;


PARTITION
: 'partition' | 'PARTITION'
;


STRAIGHT_JOIN
: 'straight_join'
;


NATURAL
: 'natural'
;


LEFT
: 'left'
;


RIGHT
: 'right'
;


OJ
: 'oj'
;


ON
: 'on' | 'ON'
;


ID
: ( 'a' .. 'z' | 'A' .. 'Z' | '_' )+
;


INT
: '0' .. '9'+
;


NEWLINE
: '\r'? '\n' -> skip
;


WS
: ( ' ' | '\t' | '\n' | '\r' )+ -> skip
;


USER_VAR
: '@' ( USER_VAR_SUBFIX1 | USER_VAR_SUBFIX2 | USER_VAR_SUBFIX3 | USER_VAR_SUBFIX4 )
;


fragment USER_VAR_SUBFIX1
: ( '`' ( ~ '`' )+ '`' )
;


fragment USER_VAR_SUBFIX2
: ( '\'' ( ~ '\'' )+ '\'' )
;


fragment USER_VAR_SUBFIX3
: ( '\"' ( ~ '\"' )+ '\"' )
;


fragment USER_VAR_SUBFIX4
: ( 'A' .. 'Z' | 'a' .. 'z' | '_' | '$' | '0' .. '9' | DOT )+
;

AS
: 'as' | 'AS'
;

APOSTROPHE
: ( '`' | '\'' )+ -> skip
;
