# Introduction

The Lief Game engine can load games that you'we written. The games are
very much like adventure games. Each game consist a couple of
situations (such *in a white room* or *on the surface of Mars*) which
a user can found her/him self in. Each such situation has a
description, a question and also a list of things. A situation also
has a list of suggestion from which the user can choose what to do
next.

Let's look at a simple example:

~~~
You're in a white room full of lamps and a door.

What would you like to do?

1. Open a enter through the door.
2. Jump up and down ten times
~~~

In the previous example (from a text mode program):

* the text *You're in a white room full of lamps and a door.* is the description

* the texts *What would you like to do?* if the question

* the texts *Open a enter through the door* and *Jump up and down ten times* are both suggestion.

For each such suggestion you can add different exits (which are other situations or the same situation) which are entered depending on the what the user have done (see score, situations, things). The situations (as specified by exits) are not visible to the user nor are the expressions controlling the actual exit choosen.

# Game syntax

# title

The title of the game.

Example: ```"title": "University game - Reach Nirvana in academia"```

# start

The situation where the game begins.

Example: ```"start": "Jupiter"```

# Situations

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

A text to show the user. Every phrase can be combined with one or many exits (see below). Depending on expressions (containing the user's score, situation count and the items picked) up different exits is choosen. These exits are not visible to the user.

Example: ```"phrase": "Enter the dark room"```

#### score

A user can get points (kept in the users's score). 

Example: ```"score": "10"```

If the user enters a situation with *"score": "10"* than the user get 10 points. If the user had score 5 before then the user would now have score 15.

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

~~~
    {
      "title": "White room",
      "description": "You're in a white room",
      "question": "What do you do?",
      "things": [
        {
          "thing": "guitar"
        }
      ],
      "suggestions": [
        {
          "phrase": "Enter",
          "exits": [
            {
              "expr": " score > 10",
              "situation": "Blue room",
              "explanation": "Good thing you had some points"
            },
            {
              "expr": "default",
              "situation": "White room"
            }
          ]
        },
        {
          "phrase": "Turn around and exit",
          "exit": "Outside"
        }
      ]
~~~

When the user enters the situation called *White room" the user is presented the title, description, question and possibly also a list of things available to pick up in the room. In the example above the user could pick up a *guitar*. Wether it is good or bad to pick up the guitar is typically hidden from the user and the guitar might be used in expression the determines which situation the user ends up in. The user, after having entered the *White room*,  is presented with two suggestions *Enter* and "Turn around and exit".

If the user chooses *Enter* the engine checks if the user's score is greater than 10 by using the expression *score > 10*. If user's score actually is greater than 10 then the users is taken to the *Blue room* situation with the explanation "Good thing you had some points". If the user's score is 10 or less than the user is (back) taken to the *White room* with no explanation.

If the user, on the other hand, went for the suggestion "Turn around and exit" the user is taken to the situation "Outside".

# Operators

## AND

Used in an expression to yield true if the expressions **both** are true, otherwise false. If you're using *AND* or *OR*" in combination in an expression we refer to Java's implementation of Predicate - in short think of an expression like ```HAS pen AND has book OR count > 4``` to look like this ```HAS pen AND (has book OR count > 4)``` 

### Syntax

```exp1 AND exp2```

### Example

```score == 10 AND situations < 20```

The above expression evaluates to true if score is exactly 10 **and** situations is less than 20.

## OR

Used in an expression to yield true if any of the expressions are true, otherwise false

### Syntax

```exp1 OR exp2```

### Example

```score == 10 OR situations < 20```

The above expression evaluates to true if score is exactly 10 **or** situations is less than 20. If you're using *AND* or *OR*" in combination in an expression we refer to Java's implementation of Predicate  - in short think of an expression like ```HAS pen AND has book OR count > 4``` to look like this ```HAS pen AND (has book OR count > 4)``` 

## HAS

Used in an expression to yield true if the user has picked up the item given as argument.

### Syntax

```HAS item```

### Example

```HAS book```

The above expression evaluates to true if the user has an item called "book".

## HASNOT

Used in an expression to yield true if the user has NOT picked up the item given as argument.

### Syntax

```HASNOT item```

### Example

```HASNOT book```

The above expression evaluates to true if the user has NOT picked up an item called "book".

# Comparators

## >

Checks if some operand is greater than another. If the first operand is greater than the second operand the expressions is evaluated to *true*.

### Syntax

```score > 10```

If the user's score is greater than 10 the expression is evaluated to *true*. Otherwise *false*.

## <

Checks if some operand is less than another. If the first operand is less than the second operand the expressions is evaluated to *true*.

### Syntax

```score < 10```

If the user's score is less than 10 the expression is evaluated to *true*. Otherwise *false*.


## ==

Checks if some operand is equal to an another. If the first operand is equal to the second operand the expressions is evaluated to *true*. Otherwise *false*.

### Syntax

```score == 10```

If the user's score is equal to 10 the expression is evaluated to *true*. Otherwise *false*.


## !=

Checks if some operand is NOT equal to an another. If the first operand is not equal to the second operand the expressions is evaluated to *true*. Otherwise *false*.

### Syntax

```score != 10```

If the user's score is not equal to 10 the expression is evaluated to *true*. Otherwise *false*.

# Game values

## score

## situations

# Examples

