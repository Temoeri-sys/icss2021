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

// ONLY SUPPORTING DECLERATIONS
BACKGROUDN_COLOR_DECLERATION: 'background-color';
COLOR_DECLERATION: 'color';
WIDTH_DECLERATION: 'width';
HEIGHT_DECLERATION: 'height';


//--- PARSER: ---
stylesheet: (variableAssignment | stylerule)*;

variableAssignment
    : variableReference
      ASSIGNMENT_OPERATOR
      (
        variableReference
        | boolLiteral
        | colorLiteral
        | pixelLiteral
      )
    ;

stylerule
    : selector
      declaration COLON (colorLiteral | pixelLiteral) SEMICOLON *
      CLOSE_BRACE
    ;

selector
    : (tagSelector | idSelector | classSelector) OPEN_BRACE
    ;

variableReference
    : CAPITAL_IDENT
    ;

boolLiteral
    : TRUE
    | FALSE
    ;

declaration
    : BACKGROUDN_COLOR_DECLERATION
    | COLOR_DECLERATION
    | WIDTH_DECLERATION
    | HEIGHT_DECLERATION
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