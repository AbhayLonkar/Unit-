package com.devabhay.unitconverter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;

public class ConverterActivity extends AppCompatActivity {

    String[] items;
    String input1, input2, tag;
    ArrayAdapter<String> arrayAdapter;
    Spinner spinner1, spinner2;
    TextView t1, t2;
    MaterialToolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_converter);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // for topAction toolbar
        toolbar = findViewById(R.id.materialToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        // for drop down menu
        spinner1 = findViewById(R.id.spinner);
        spinner2 = findViewById(R.id.spinner2);
        t1 = findViewById(R.id.input1);
        t2 = findViewById(R.id.input2);

        initItems();

        arrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_list, items);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_list);
        spinner1.setAdapter(arrayAdapter);
        spinner2.setAdapter(arrayAdapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                input1 = parent.getItemAtPosition(position).toString();
                if (input2 != null) {
                    cal(t1.getText().toString());
                }
                Log.i("info",input1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                input2 = parent.getItemAtPosition(position).toString();
                cal(t1.getText().toString());
                Log.i("info",input2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void initItems() {
        Intent intent = getIntent();
        tag = intent.getStringExtra("tag");
        switch (tag) {
            case "Length":
                items = new String[]{"Kilometer (km)", "Meter (m)", "Centimeter (cm)", "Millimeter (mm)"};
                break;
            case "Currency":
                items = new String[]{"Indian Rupee INR", "US Dollar USD", "Euro EUR", "Japanese Yen JPY"};
                break;
            case "Area":
                items = new String[]{"Square Kilometer (km²)", "Hectare (ha)", "Are (ar)", "Square meter (m²)"};
                break;
            case "Volume":
                items = new String[]{"Hectoliters (hl)", "Liter (l)", "Deciliter (dl)", "Milliliter (ml)"};
                break;
            case "Weight":
                items = new String[]{"Kilogram (kg)", "Gram (g)", "Pound (lib)", "Ton (t)"};
                break;
            case "Temperature":
                items = new String[]{"Degree Celsius (℃)", "Degree Fahrenheit (℉)", "Kelvin (K)"};
                break;
            case "Speed":
                items = new String[]{"Meter/second (m/s)", "Kilometer/second (km/s)", "Kilometer/hour (km/h)", "Speed of light (c)"};
                break;
            case "Pressure":
                items = new String[]{"Standard atmosphere (atm)", "Hectopascal (hPa)", "Kilopascal (kPa)", "Megapascal (mPa)"};
                break;
            case "Power":
                items = new String[]{"Kilowatt (kW)", "Watt (W)", "Joule/second (J/s)", "Imperial horsepower (hp)"};
                break;
            default:
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(tag + " Conversion");
        }
    }


    public void insert(View view) {
        Button button = (Button) view;
        String btnText = button.getText().toString();
        if (btnText.equals("AC")) {
            t1.setText("0");
            t2.setText("0");
            return;
        }
        String num = t1.getText().toString();
        if (btnText.equals("X")) {
            if (num.length() == 1) {
                num = "0";
                Log.i("info", num);
            } else {
                num = num.substring(0, num.length() - 1);
                Log.i("info", num);
            }
        } else if (btnText.equals(".")) {
            if (num.contains(".")) {
                return;
            } else {
                num += btnText;
            }
        } else {
            if (num.equals("0")) {
                num = "";
            }
            num += button.getText().toString();
            Log.i("info", num);
        }
        t1.setText(num);
        cal(num);
    }

    public void cal(String num) {
        double res = 0.0;
        if (tag.equals("Length")) {
            res = lengthConversion(num);
        } else if (tag.equals("Currency")) {
            res = currencyConversion(num);
        } else if (tag.equals("Area")) {
            res = areaConversion(num);
        } else if (tag.equals("Volume")) {
            res = volumeConversion(num);
        } else if (tag.equals("Weight")) {
            res = weightConversion(num);
        } else if (tag.equals("Temperature")) {
            res = temperatureConversion(num);
        } else if (tag.equals("Speed")) {
            res = speedConversion(num);
        } else if (tag.equals("Pressure")) {
            res = pressureConversion(num);
        } else if (tag.equals("Power")) {
            res = powerConversion(num);
        }

        String finalOutput = String.valueOf(res);
        if (finalOutput.contains("E")) {
            finalOutput = String.format("%.10f",res);
        }

        t2.setText(finalOutput);
    }

    private double powerConversion(String num) {
        double inp1 = Double.parseDouble(num);
        double res = 0.0;

        if (input1.equals("Kilowatt (kW)")) {
            if (input2.equals("Watt (W)")) {
                res = inp1 * 1000;
            } else if (input2.equals("Joule/second (J/s)")) {
                res = inp1 * 1000;
            } else if (input2.equals("Imperial horsepower (hp)")) {
                res = inp1 * 1.34102;
            } else {
                res = inp1;
            }
        } else if (input1.equals("Watt (W)")) {
            if (input2.equals("Kilowatt (kW)")) {
                res = inp1 / 1000;
            } else if (input2.equals("Joule/second (J/s)")) {
                res = inp1;
            } else if (input2.equals("Imperial horsepower (hp)")) {
                res = inp1 * 0.00134102;
            } else {
                res = inp1;
            }
        } else if (input1.equals("Joule/second (J/s)")) {
            if (input2.equals("Kilowatt (kW)")) {
                res = inp1 / 1000;
            } else if (input2.equals("Watt (W)")) {
                res = inp1;
            } else if (input2.equals("Imperial horsepower (hp)")) {
                res = inp1 * 0.00134102;
            } else {
                res = inp1;
            }
        } else if (input1.equals("Imperial horsepower (hp)")) {
            if (input2.equals("Kilowatt (kW)")) {
                res = inp1 * 0.7457;
            } else if (input2.equals("Watt (W)")) {
                res = inp1 * 745.7;
            } else if (input2.equals("Joule/second (J/s)")) {
                res = inp1 * 745.7;
            } else {
                res = inp1;
            }
        }

        return res;
    }


    private double pressureConversion(String num) {
        double inp1 = Double.parseDouble(num);
        double res = 0.0;

        if (input1.equals("Standard atmosphere (atm)")) {
            if (input2.equals("Hectopascal (hPa)")) {
                res = inp1 * 1013.25;
            } else if (input2.equals("Kilopascal (kPa)")) {
                res = inp1 * 101.325;
            } else if (input2.equals("Megapascal (mPa)")) {
                res = inp1 * 0.101325;
            } else {
                res = inp1;
            }
        } else if (input1.equals("Hectopascal (hPa)")) {
            if (input2.equals("Standard atmosphere (atm)")) {
                res = inp1 / 1013.25;
            } else if (input2.equals("Kilopascal (kPa)")) {
                res = inp1 / 10;
            } else if (input2.equals("Megapascal (mPa)")) {
                res = inp1 / 10000;
            } else {
                res = inp1;
            }
        } else if (input1.equals("Kilopascal (kPa)")) {
            if (input2.equals("Standard atmosphere (atm)")) {
                res = inp1 / 101.325;
            } else if (input2.equals("Hectopascal (hPa)")) {
                res = inp1 * 10;
            } else if (input2.equals("Megapascal (mPa)")) {
                res = inp1 / 1000;
            } else {
                res = inp1;
            }
        } else if (input1.equals("Megapascal (mPa)")) {
            if (input2.equals("Standard atmosphere (atm)")) {
                res = inp1 * 9.8692;
            } else if (input2.equals("Hectopascal (hPa)")) {
                res = inp1 * 10000;
            } else if (input2.equals("Kilopascal (kPa)")) {
                res = inp1 * 1000;
            } else {
                res = inp1;
            }
        }
        return res;
    }


    private double speedConversion(String num) {
        double inp1 = Double.parseDouble(num);
        double res = 0.0;

        if (input1.equals("Meter/second (m/s)")) {
            if (input2.equals("Kilometer/second (km/s)")) {
                res = inp1 / 1000;
            } else if (input2.equals("Kilometer/hour (km/h)")) {
                res = inp1 * 3.6;
            } else if (input2.equals("Speed of light (c)")) {
                res = inp1 / 299792458;
            } else {
                res = inp1;
            }
        } else if (input1.equals("Kilometer/second (km/s)")) {
            if (input2.equals("Meter/second (m/s)")) {
                res = inp1 * 1000;
            } else if (input2.equals("Kilometer/hour (km/h)")) {
                res = inp1 * 3600;
            } else if (input2.equals("Speed of light (c)")) {
                res = inp1 / 299792.458;
            } else {
                res = inp1;
            }
        } else if (input1.equals("Kilometer/hour (km/h)")) {
            if (input2.equals("Meter/second (m/s)")) {
                res = inp1 / 3.6;
            } else if (input2.equals("Kilometer/second (km/s)")) {
                res = inp1 / 3600;
            } else if (input2.equals("Speed of light (c)")) {
                res = inp1 / 1079252848.8;
            } else {
                res = inp1;
            }
        } else if (input1.equals("Speed of light (c)")) {
            if (input2.equals("Meter/second (m/s)")) {
                res = inp1 * 299796138;
            } else if (input2.equals("Kilometer/second (km/s)")) {
                res = inp1 * 299796.138;
            } else if (input2.equals("Kilometer/hour (km/h)")) {
                res = inp1 * 1079266099.05;
            } else {
                res = inp1;
            }
        }
        return res;
    }


    private double temperatureConversion(String num) {
        double inp1 = Double.parseDouble(num);
        double res = 0.0;

        if (input1.equals("Degree Celsius (℃)")) {
            if (input2.equals("Degree Fahrenheit (℉)")) {
                res = (inp1 * 9/5) + 32;
            } else if (input2.equals("Kelvin (K)")) {
                res = inp1 + 273.15;
            } else {
                res = inp1;
            }
        } else if (input1.equals("Degree Fahrenheit (℉)")) {
            if (input2.equals("Degree Celsius (℃)")) {
                res = (inp1 - 32) * 5/9;
            } else if (input2.equals("Kelvin (K)")) {
                res = (inp1 - 32) * 5/9 + 273.15;
            } else {
                res = inp1;
            }
        } else if (input1.equals("Kelvin (K)")) {
            if (input2.equals("Degree Celsius (℃)")) {
                res = inp1 - 273.15;
            } else if (input2.equals("Degree Fahrenheit (℉)")) {
                res = (inp1 - 273.15) * 9/5 + 32;
            } else {
                res = inp1;
            }
        }
        return res;
    }


    private double weightConversion(String num) {
        double inp1 = Double.parseDouble(num);
        double res = 0.0;

        if (input1.equals("Kilogram (kg)")) {
            if (input2.equals("Gram (g)")) {
                res = inp1 * 1000;
            } else if (input2.equals("Pound (lib)")) {
                res = inp1 * 2.20462;
            } else if (input2.equals("Ton (t)")) {
                res = inp1 / 1000;
            } else {
                res = inp1;
            }
        } else if (input1.equals("Gram (g)")) {
            if (input2.equals("Kilogram (kg)")) {
                res = inp1 / 1000;
            } else if (input2.equals("Pound (lib)")) {
                res = inp1 * 0.00220462;
            } else if (input2.equals("Ton (t)")) {
                res = inp1 / 1000000;
            } else {
                res = inp1;
            }
        } else if (input1.equals("Pound (lib)")) {
            if (input2.equals("Kilogram (kg)")) {
                res = inp1 * 0.453592;
            } else if (input2.equals("Gram (g)")) {
                res = inp1 * 453.592;
            } else if (input2.equals("Ton (t)")) {
                res = inp1 * 0.000453592;
            } else {
                res = inp1;
            }
        } else if (input1.equals("Ton (t)")) {
            if (input2.equals("Kilogram (kg)")) {
                res = inp1 * 1000;
            } else if (input2.equals("Gram (g)")) {
                res = inp1 * 1000000;
            } else if (input2.equals("Pound (lib)")) {
                res = inp1 * 2204.62;
            } else {
                res = inp1;
            }
        }
        return res;
    }


    private double volumeConversion(String num) {
        double  inp1, inp2, res = 0.0;
        inp1 = Double.parseDouble(num);
        if (input1.equals("Hectoliters (hl)")) {
            if (input2.equals("Liter (l)")) {
                res = inp1 * 100;
            } else if (input2.equals("Deciliter (dl)")) {
                res = inp1 * 1000;
            } else if (input2.equals("Milliliter (ml)")) {
                res = inp1 * 100000;
            } else {
                res = inp1;
            }
        } else if (input1.equals("Liter (l)")) {
            if (input2.equals("Hectoliters (hl)")) {
                res = inp1 / 100;
            } else if (input2.equals("Deciliter (dl)")) {
                res = inp1 * 10;
            } else if (input2.equals("Milliliter (ml)")) {
                res = inp1 * 1000;
            } else {
                res = inp1;
            }
        } else if (input1.equals("Deciliter (dl)")) {
            if (input2.equals("Hectoliters (hl)")) {
                res = inp1 / 1000;
            } else if (input2.equals("Liter (l)")) {
                res = inp1 / 10;
            } else if (input2.equals("Milliliter (ml)")) {
                res = inp1 * 100;
            } else {
                res = inp1;
            }
        } else if (input1.equals("Milliliter (ml)")) {
            if (input2.equals("Hectoliters (hl)")) {
                res = inp1 / 100000;
            } else if (input2.equals("Liter (l)")) {
                res = inp1 / 1000;
            } else if (input2.equals("Deciliter (dl)")) {
                res = inp1 / 100;
            } else {
                res = inp1;
            }
        }
        return res;
    }

    private double areaConversion(String num) {
        double  inp1, inp2, res = 0.0;
        inp1 = Double.parseDouble(num);
        if (input1.equals("Square Kilometer (km²)")) {
            if (input2.equals("Hectare (ha)")) {
                res = inp1 * 100;
            } else if (input2.equals("Are (ar)")) {
                res = inp1 * 10000;
            } else if (input2.equals("Square meter (m²)")) {
                res = inp1 * 1000000;
            } else {
                res = inp1;
            }
        } else if (input1.equals("Hectare (ha)")) {
            if (input2.equals("Square Kilometer (km²)")) {
                res = inp1 / 100;
            } else if (input2.equals("Are (ar)")) {
                res = inp1 * 100;
            } else if (input2.equals("Square meter (m²)")) {
                res = inp1 * 10000;
            } else {
                res = inp1;
            }
        } else if (input1.equals("Are (ar)")) {
            if (input2.equals("Square Kilometer (km²)")) {
                res = inp1 / 10000;
            } else if (input2.equals("Hectare (ha)")) {
                res = inp1 / 100;
            } else if (input2.equals("Square meter (m²)")) {
                res = inp1 * 100;
            } else {
                res = inp1;
            }
        } else if (input1.equals("Square meter (m²)")) {
            if (input2.equals("Square Kilometer (km²)")) {
                res = inp1 / 1000000;
            } else if (input2.equals("Hectare (ha)")) {
                res = inp1 / 10000;
            } else if (input2.equals("Are (ar)")) {
                res = inp1 / 100;
            } else {
                res = inp1;
            }
        }
        return res;
    }

    private double currencyConversion(String num) {
        double  inp1, inp2, res = 0.0;
        inp1 = Double.parseDouble(num);
        if (input1.equals("Indian Rupee INR")) {
            if (input2.equals("US Dollar USD")) {
                res = inp1 / 83.9534 ;
            } else if (input2.equals("Euro EUR")) {
                res = inp1 / 92.7321 ;
            } else if (input2.equals("Japanese Yen JPY")) {
                res = inp1 * 1.6878 ;
            } else {
                res = inp1;
            }
        } else if (input1.equals("US Dollar USD")) {
            if (input2.equals("Indian Rupee INR")) {
                res = inp1 * 83.9534;
            }  else if (input2.equals("Euro EUR")) {
                res = inp1 / 1.1045;
            } else if (input2.equals("Japanese Yen JPY")) {
                res = inp1 * 141.6635;
            } else {
                res = inp1;
            }
        } else if (input1.equals("Euro EUR")) {
            if (input2.equals("Indian Rupee INR")) {
                res = inp1 * 92.7168;
            } else if (input2.equals("US Dollar USD")) {
                res = inp1 * 1.1044;
            } else if (input2.equals("Japanese Yen JPY")) {
                res = inp1 * 156.4576;
            } else {
                res = inp1;
            }
        } else if (input1.equals("Japanese Yen JPY")) {
            if (input2.equals("Indian Rupee INR")) {
                res = inp1 / 1.6829;
            } else if (input2.equals("US Dollar USD")) {
                res = inp1 / 141.2149;
            } else if (input2.equals("Euro EUR")) {
                res = inp1 / 156.0149;
            } else {
                res = inp1;
            }
        }
        return res;
    }

    public double lengthConversion(String num) {
        double  inp1, inp2, res = 0.0;
        inp1 = Double.parseDouble(num);

        if (input1.equals("Kilometer (km)"))  {
            if (input2.equals("Meter (m)")){
                res = inp1 * 1000;
            } else if (input2.equals("Centimeter (cm)")) {
                res = inp1 * 100000;
            } else if (input2.equals("Millimeter (mm)")) {
                res = inp1 * 1000000;
            } else {
                res = inp1;
            }
        }
        else if (input1.equals("Centimeter (cm)"))  {
            if (input2.equals("Kilometer (km)")){
                res = inp1 / 100000;
            } else if (input2.equals("Meter (m)")) {
                res = inp1 / 100;
            } else if (input2.equals("Millimeter (mm)")) {
                res = inp1 / 10;
            } else {
                res = inp1;
            }
        }
        else if (input1.equals("Meter (m)"))  {
            if (input2.equals("Kilometer (km)")){
                res = inp1 / 1000;
            } else if (input2.equals("Centimeter (cm)")) {
                res = inp1 * 100;
            } else if (input2.equals("Millimeter (mm)")) {
                res = inp1 * 1000;
            } else {
                res = inp1;
            }
        }
        else if (input1.equals("Millimeter (mm)"))  {
            if (input2.equals("Kilometer (km)")){
                res = inp1 / 1000000;
            } else if (input2.equals("Centimeter (cm)")) {
                res = inp1 / 10;
            } else if (input2.equals("Meter (m)")) {
                res = inp1 / 1000;
            } else {
                res = inp1;
            }
        }
        return res;
    }

}