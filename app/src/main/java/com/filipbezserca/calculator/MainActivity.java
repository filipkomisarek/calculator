package com.filipbezserca.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final String DISPLAY_KEY = "display";
    public static final String ACCUMULATOR_KEY = "accumulator";
    public static final String OPERATION_KEY = "operation";
    private String display = "0";
    private double accumulator = 0.0;

    private Operation currentOperation = Operation.NONE;
    private TextView displayTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayTextView =   (TextView) findViewById(R.id.textView3);
    }

    @Override
    protected void onSaveInstanceState (Bundle outState) { //metoda potrzebna przy obracaniu ekranu do zapisania zawartosci ekranu
        //Bunde - mapa która przechoduje obiekty do ktorych dodawane sa klucze przez ktore mozna je wywolac
        super.onSaveInstanceState(outState);
        outState.putString(DISPLAY_KEY, display); //zapamietanie zawartosci wyswietlacza, DISPLAY_KEY [jest to klucz z Bundla] to string zamieniony na stała, 'display' - jak ma byc zawartość
        // ctrl+alt+C - zamiana np strina na stałą np DISPLAY_KEY
        outState.putDouble(ACCUMULATOR_KEY, accumulator); //zapamietaj accumulator
        outState.putString(OPERATION_KEY, currentOperation.name()); //zapamietaj operation / niemozna zapamietac operation bo to enum ale mozna je rozponac po nazwie, dlatego .name()
    }

    @Override
    protected void onRestoreInstanceState (Bundle savedInstanceState) { //metoda potrzebna przy obracaniu ekranu do przywrocenia zawartosc iekranu
        super.onRestoreInstanceState(savedInstanceState); //zapamietaj display
        display = savedInstanceState.getString(DISPLAY_KEY, "0"); //odzyskaj display; zostaje przywrocna zawartosc ekranu poprzez wywolanie klucza DISPLAY_KEY, lub wartość domyslna '0', wszystko zapisane do zmiennej display
        accumulator = savedInstanceState.getDouble(ACCUMULATOR_KEY); //wyciagniecie accumulator, nie potrzeba robic wartosci domyslnej, bo bedzie '0,0'
        currentOperation = Operation.valueOf(savedInstanceState.getString(OPERATION_KEY)); //wyciaga currentOperation, Operation.valueOf - metoda enuma ktora zwraca na podstawie nazwy konkretny obiekt

        updateDisplay(); //wyświetl dane na ekranie
    }

    public void keyClicked(View view) {

        Button button = (Button) view;
        String key = button.getText().toString();

        switch (key) {
            case "0":
            case "1":
            case "2":
            case "3":
            case "4":
            case "5":
            case "6":
            case "7":
            case "8":
            case "9":
                if(display.equals("0")){
                    display = "";
                }
                display += key;
                break;
            case ".":
                if(!display.contains(".")) { //jeśli ekran nie zawiera . to
                display += key; //dopisz . na wyświetlaczu
                }
                break;
            case "+":
            case "-":
            case "*":
            case "/":
                calculateOperation(key);
                break;
            case "SQRT(x)":
                calculateSquareRoot();
                break;
            case "x^2":
                calculateSquare();
                break;
            case "=":
                calculateResult();
                break;
            case "CE":
                clearOne();
                break;
            case "C":
                clearAll();
                break;

        }
        updateDisplay(); //metoda wyswietlajaca dane na ekranie

    }

    private void calculateSquare() {
        double displayValue = Double.parseDouble(display);//przekształc wartosc na 'double'
        displayResult(displayValue * displayValue);//wyświetl wynik - value * value
    }

    private void calculateSquareRoot() {
        double displayValue = Double.parseDouble(display);//przekształc wartosc na 'double'
        displayResult(Math.sqrt(displayValue));//wyświetl wynik na ekranie stosujac metode javy Math.sqrt
    }

    private void updateDisplay() { //metoda wyswietlajaca dane na ekranie
        displayTextView.setText(display);
    }

    private void clearAll() { //metoda czysząca cały ekran
        display = "0"; //ustaw na wyświetlaczu 0
        accumulator = 0.0; //wyzeruj accumulator
        currentOperation = Operation.NONE; //aktualmną operacje ustaw na NONE
    }

    private void clearOne() { //metoda usuwajaca ostatni znak na wuświetlaczu
        if (display.length() > 1) { //jeśli na ekranie więcej niż 1 znak
            display = display.substring(0, display.length() - 1); //tworzymy nowy string na podstawie istniejącego i określamy jego długosc, zaczynamy od 0 a kończymy na dotychczasowy minus 1
        } else {
            display = "0"; //w innym wypadku wyświetl 0
        }
    }

    private void calculateResult() {
        double displayValue = Double.parseDouble(display);
        switch (currentOperation) {
            case ADD:
                displayResult(accumulator + displayValue);
                break;
            case SUBSTRACT:
                displayResult(accumulator - displayValue);
                break;
            case MULTIPLY:
                displayResult(accumulator * displayValue);
                break;
            case DIVIDE:
                displayResult(accumulator / displayValue);
                break;
        }
        accumulator = 0.0;
        currentOperation = Operation.NONE;
    }

    private void displayResult(double result) {
        if (result == (long)result){
            display = String.format("%d", (long)result);
        } else {
            display = String.format("%s", result);
        }
    }

    private void calculateOperation(String key) {
        currentOperation = Operation.operationFromKey(key);
        accumulator = Double.parseDouble(display);
        display = "0";
    }
}
