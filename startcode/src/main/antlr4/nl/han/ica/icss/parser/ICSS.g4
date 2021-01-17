grammar ICSS;

//--- LEXER: ---

// IF support:
IF: 'if';
ELSE: 'else';
BOX_BRACKET_OPEN: '[';
BOX_BRACKET_CLOSE: ']';

//Literals
TRUE: 'TRUE';
FALSE: 'FALSE';
PIXELSIZE: [0-9]+ 'px';
PERCENTAGE: [0-9]+ '%';
SCALAR: [0-9]+;

//Color value takes precedence over id idents
COLOR: '#' [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f];

//Specific identifiers for id's and css classes
ID_IDENT: '#' [a-z0-9\-]+;
CLASS_IDENT: '.' [a-z0-9\-]+;

//General identifiers
LOWER_IDENT: [a-z] [a-z0-9\-]*;
CAPITAL_IDENT: [A-Z] [A-Za-z0-9_]*;

//All whitespace is skipped
WS: [ \t\r\n]+ -> skip;

// Basic elements
OPEN_BRACE: '{';
CLOSE_BRACE: '}';
SEMICOLON: ';';
COLON: ':';
PLUS: '+';
MIN: '-';
MUL: '*';
ASSIGNMENT_OPERATOR: ':=';

// Copyright to Temoeri Sjamojani

//--- PARSER: ---
stylesheet: (variableAssignment | stylerule)*;

variableAssignment
    : variableReference
      ASSIGNMENT_OPERATOR
      (
        variableReference
        | boolLiteral
        | scalarLiteral
        | colorLiteral
        | pixelLiteral
        | percentageLiteral
        | addOperation
        | subtractOperation
        | multiplyOperation
      )
      SEMICOLON
    ;

stylerule
    : (tagSelector | idSelector | classSelector)
      OPEN_BRACE
      (declaration | ifClause)*
      CLOSE_BRACE
    ;


ifClause
    : IF BOX_BRACKET_OPEN variableReference BOX_BRACKET_CLOSE OPEN_BRACE
      (ifClause | declaration | elseClause)*
      CLOSE_BRACE
    ;

elseClause
    : CLOSE_BRACE ELSE OPEN_BRACE
      declaration
    ;

declaration
    : property
      COLON
      (
        colorLiteral
        | pixelLiteral
        | percentageLiteral
        | variableReference
        | scalarLiteral
        | addOperation
        | subtractOperation
        | multiplyOperation
       )
      SEMICOLON
    ;

addOperation
    : (
      	scalarLiteral
      	| pixelLiteral
      	| percentageLiteral
      	| pixelLiteral
      	| variableReference
      )
      PLUS
      (
          scalarLiteral
          | pixelLiteral
          | percentageLiteral
          | pixelLiteral
          | variableReference
          | multiplyOperation
          | subtractOperation
          | addOperation
      )
    ;

subtractOperation
    : (
      	scalarLiteral
      	| pixelLiteral
      	| percentageLiteral
      	| pixelLiteral
      	| variableReference
      )
      MIN
      (
          scalarLiteral
          | pixelLiteral
          | percentageLiteral
          | pixelLiteral
          | variableReference
          | multiplyOperation
          | subtractOperation
          | addOperation
      )
    ;

multiplyOperation
    : (
      	scalarLiteral
      	| pixelLiteral
      	| percentageLiteral
      	| pixelLiteral
      	| variableReference
      )
      MUL
      (
          scalarLiteral
          | pixelLiteral
          | percentageLiteral
          | pixelLiteral
          | variableReference
          | multiplyOperation
          | subtractOperation
          | addOperation
      )
    ;

variableReference
    : CAPITAL_IDENT
    ;

boolLiteral
    : TRUE
    | FALSE
    ;

tagSelector
    : LOWER_IDENT
    ;

idSelector
    : ID_IDENT
    ;

classSelector
    : CLASS_IDENT
    ;

colorLiteral
    : COLOR
    ;

pixelLiteral
    : PIXELSIZE
    ;

percentageLiteral
    :  PERCENTAGE
    ;

property
    : LOWER_IDENT
    ;

scalarLiteral
    : SCALAR
    ;