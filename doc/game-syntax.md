
# Basic

## title

The title of the game.

Example: ```"title": "University game - Reach Nirvana in academia"```

## start

The situation where the game begins.

Example: ```"start": "Jupiter"```

## Situations

A list of situations (see situation).

## Situation

### title

The title (name) of the situation.

Example: ```"title": "Lecture hall"```

### description

Text describing the situation

Example: ```"description": "You're in a hall with ents in it"```

### questions

A question put to the user

Example: ```"question": "What would you like to do?"```

### suggestions

A list of suggestion on what to do to answer the question. 

### suggestion

#### phrase

A text show to the user. Every phrase can have one or many exits. Depending on expressions (containing the user's score, situation count and the items picked) up different exits is choosen. These exits are not visible to the user.

Example: ```"phrase": "Enter the dark room"```

#### exits

A list of possible exits. Each exit consists of

**expr** - An expression evaluated when the user is in the situation and having choosen the phrase (e g "Enter the dark room").

Example: ```"expr": "score > 10"```

**situation**

If the expression (as defined by *expr* above) is evaluated to true this situation is automatically entered.

Example: ```"situation": "Dark room"```

**explanation**

Text to present to the user to give a hint on why a situation was entered.

Example: ```"explanation": "You can't enter the Dark room without a lighter"```

### things

A list of things initially available for the user (if not picked up by the used during the game)

Example: ```"things": "[ "thing": "pen", "thing": paper"]"```

## Situation example



# Operators

## AND

Used in an expression to yield true if the expressions **both** are true, otherwise false

### Syntax

```exp1 AND exp2```

### Example

```score == 10 AND situations < 20```

The above expression evaluates to true if score is exactly 10 **and** situations is less than 20.

## OR

Used in an expression to yield true if any of the expressions are true, otherwise false

### Syntax

```exp1 AND exp2```

### Example

```score == 10 OR situations < 20```

The above expression evaluates to true if score is exactly 10 **or** situations is less than 20.

## HAS

Used in an expression to yield true if the user has picked up the item given as argument.

### Syntax

```HAS item```

### Example

```HAS book```

The above expression evaluates to true if the user has an item called "book".

# Comparators

## >

## <

## ==

## !=

# Game values

## score

## situations

# Examples

