lexer grammar Javalex;
options {
  language = Java;
}
@header {package com.pab.jmxmonitor; }


RULE: ;

WS  : (' '|'\r'|'\t'|'\u000C'|'\n') {$channel=HIDDEN;}
    ;
COMMENT
    : '/*' .* '*/' {$channel=HIDDEN;}
    ;

ID : ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')* ;

OPAREN: '(';
CPAREN: ')';

STRING: '"' .* '"';

INTEGER: '0'..'9' ('0'..'9')*;

COMMA: ',';

DOT: '.';
