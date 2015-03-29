
//pins that use ardumotor board
int pwm_a = 3;   //power control pin 3 (arm power)
int pwm_b = 11;  //power control pin (rotation plate)
int dir_a = 12;  //rotation plate  pin 12 (direction)
int dir_b = 13;  // arm movement arm up/arm down pin 13(direction)

//pins for the 2 motors to flip cube
int motoa1 = 9;      
int motoa2 = 8; //opposite direction

int motob1 = 7; 
int motob2 = 6; //opposite direction

int analogPin= 0;     // potentiometer wiper (middle terminal) connected to analog pin 0
int analogPin2= 1;     // potentiometer wiper (middle terminal) connected to analog pin 1

int val = 0;           // variable to store the value read for analogPin
int val2 = 0;           // variable to store the value read for analogPin2

//set arduno to output to the piuns attached the motors
void setup(){
  pinMode(pwm_a, OUTPUT);  //Set control pins to be outputs
  pinMode(pwm_b, OUTPUT);
  pinMode(dir_a, OUTPUT);
  pinMode(dir_b, OUTPUT);
  
  pinMode(motoa2 , OUTPUT);  
  pinMode(motoa1 , OUTPUT);  
  pinMode(motob1 , OUTPUT);  
  pinMode(motob2 , OUTPUT);  
}

void loop(){
FlickOverCube();RotateCube90Clockwise();FlickOverCube();FlickOverCube();RotateCube180();FlickOverCube();FlickOverCube();FlickOverCube();RotateCube180();MoveCube90Clockwise();FlickOverCube();RotateCube90AntiClockwise();MoveCube90Clockwise();FlickOverCube();RotateCube180();FlickOverCube();FlickOverCube();FlickOverCube();RotateCube90AntiClockwise();MoveCube90AntiClockwise();FlickOverCube();RotateCube90Clockwise();FlickOverCube();RotateCube180();FlickOverCube();FlickOverCube();FlickOverCube();RotateCube90AntiClockwise();MoveCube90AntiClockwise();FlickOverCube();RotateCube90Clockwise();
  
while(1);
}


//roate a face on the cube 90 degrees clockwise
void RotateCube90Clockwise(){
ArmUpClockwise();
  digitalWrite(dir_b, LOW); 
  bstart();
  delay(3200);

  while(true){
    delay(50);
    val = analogRead(analogPin);    // read the input pin
    val2 = analogRead(analogPin2);    // read the input pin
    if (val > 800 || val2 > 600) { //when sensor reaches these values stop rotation cube
      delay(600);
      bstop();
      delay(500);
      ArmDown();
      delay(500);
      digitalWrite(dir_b, HIGH); 
      bstart();
      delay(500);
      bstop();
      break;
    }
  }  
}

//rotate a face on the cube 90 degrees anti clockwise
void RotateCube90AntiClockwise(){
  ArmUpAntiClockwise();
  digitalWrite(dir_b, HIGH); 
  bstart();
  delay(2800);

  while(true){
    delay(50);
    val = analogRead(analogPin);    // read the input pin
    val2 = analogRead(analogPin2);    // read the input pin
    if (val > 800 || val2 > 600) {
      delay(900);
      bstop();
      delay(500);
      ArmDown();
      delay(500);
      digitalWrite(dir_b, LOW); 
      bstart();
      delay(300);
      bstop();
      break;
      break;
    }
  }  
}

//rotate face on cube 180 degrees
void RotateCube180(){
  ArmUpAntiClockwise();
  digitalWrite(dir_b, HIGH); 
  bstart();
  delay(7000);

  while(true){
    delay(50);
    val = analogRead(analogPin);    // read the input pin
    val2 = analogRead(analogPin2);    // read the input pin
    if (val > 800 || val2 > 600) {
      delay(900);
      bstop();
      delay(500);
      ArmDown();
      delay(500);
      digitalWrite(dir_b, LOW); 
      bstart();
      delay(300);
      bstop();
      break;
      break;
    }
  }   
}


//move arm up that hold the top part of cube in place
void ArmUpAntiClockwise(){
  analogWrite(pwm_a, 255);
  
  astart();
  digitalWrite(dir_a, LOW); 
  delay(1000);
  
  bstart();
  digitalWrite(dir_b, LOW); 
  delay(600);
  digitalWrite(dir_b, HIGH); 
  delay(800);
  digitalWrite(dir_b, LOW); 
  delay(900);  

  bstop();
}


//move arm up that hold the top part of cube in place
void ArmUpClockwise(){
  analogWrite(pwm_a, 255);
  
  astart();
  digitalWrite(dir_a, LOW); 
  delay(1000);
  
  bstart();
  digitalWrite(dir_b, HIGH); 
  delay(600);
  digitalWrite(dir_b, LOW); 
  delay(800);
  digitalWrite(dir_b, HIGH); 
  delay(900);  

  bstop();
}

//move arm down from cube int default position
void ArmDown(){
  astart();
  digitalWrite(dir_a, HIGH); 
  delay(800);
  analogWrite(pwm_a, 30);
  delay(400);
  astop();
}

//move entire cube 90 degrees anti clockwise
void MoveCube90AntiClockwise(){
  bstart();
  digitalWrite(dir_b, HIGH); 
  delay(2000);
  while(true){
    delay(50);
    val = analogRead(analogPin);    // read the input pin
    val2 = analogRead(analogPin2);    // read the input pin
    if (val > 800 || val2 > 600) {//when sensor reaches these values stop rotation cube
      digitalWrite(dir_b, HIGH); 
      delay(200);
      bstop(); 
      break;
    }
  }  
}

//move entire cube clockwise
void MoveCube90Clockwise(){
  bstart();
  digitalWrite(dir_b, LOW); 
  delay(2000);
  while(true){
    delay(50);
    val = analogRead(analogPin);    // read the input pin
    val2 = analogRead(analogPin2);    // read the input pin
    if (val > 800 || val2 > 600) {//when sensor reaches these values stop rotation cube
      delay(400);
      bstop(); 
      break;
    }
  }  
}

//move entire cube 180 degrees
void MoveCube180(){
  bstart();
  digitalWrite(dir_b, HIGH); 
  delay(5000);
  while(true){
    delay(50);
    val = analogRead(analogPin);    // read the input pin
    val2 = analogRead(analogPin2);    // read the input pin
    if (val > 800 || val2 > 600) {
      digitalWrite(dir_b, HIGH); 
      delay(200);
      bstop(); 
      break;
    }
  }  
}

// stop motor a
void astop(){
  analogWrite(pwm_a, 0);   
}

// stop motor b
void bstop(){
  analogWrite(pwm_b, 0);    
}

//start motor a
void astart(){ 
  analogWrite(pwm_a, 100);   
}

//start motorr b
void bstart(){ 
  analogWrite(pwm_b, 255);  
}

//lift flicker arm without flipping cube
void LiftFlicker(){
  digitalWrite(motoa1, HIGH);   
  delay(100);
  digitalWrite(motoa1, LOW);  
  digitalWrite(motob2, HIGH);  
  delay(1000);
  digitalWrite(motob2, LOW);   
  digitalWrite(motoa2, HIGH); 
  delay(30);
  digitalWrite(motoa2, LOW);   
  digitalWrite(motob2, HIGH);  
  delay(1000);
  digitalWrite(motob2, LOW);  
}

//lower flicker arm into flip position
void LowerFlicker(){
  digitalWrite(motoa1, HIGH);  
  delay(100);                 
  digitalWrite(motoa1, LOW);  
  delay(1000);  
}


//flip over cube
void FlickOverCube(){
  LowerFlicker();
  
  digitalWrite(motoa2, HIGH);  
  delay(100);                  
  digitalWrite(motoa2, LOW);   
  digitalWrite(motoa1, HIGH); 
  digitalWrite(motob1, HIGH);   
  delay(1000);                
  digitalWrite(motoa1, LOW);   
  digitalWrite(motob1, LOW);  
  
  LiftFlicker();
}