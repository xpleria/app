/*
 * Copyright (c) 2015, Marjan Krsteski, Michael Madume and Neil Patrao
 * University of Southampton.
 * All rights reserved.
 *
 * Redistribution and use with or without modification,
 * are permitted provided that the following condition is met:
 *
 * o Redistributions of source code must retain the above copyright notice, this
 *   condition and the following disclaimer.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package uk.ac.soton.ecs.np5g15.ftditerminal;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

public class Terminal extends AppCompatActivity {

    private TextView terminal;
    private ScrollView scrollView;
    private EditText editText;
    private String terminalText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        terminal = (TextView)  findViewById(R.id.terminal);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        editText = (EditText)  findViewById(R.id.editText);

        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/tex.otf");
        terminal.setTypeface(typeFace);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.getString(R.string.terminal));
        terminalText = stringBuilder.toString();
        stringBuilder.append('_');
        terminal.setText(stringBuilder.toString());

        terminal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });



        editText.requestFocus();
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String lastChar = editText.getText().toString();
                if (lastChar.length() != 0) {
                    lastChar = lastChar.substring(lastChar.length() - 1);
                }
                if (lastChar.equals("\n")) {
                    executeCommand(editText.getText().toString());
                } else {
                    displayCommand(editText.getText().toString());
                }
            }
        });

    }

    private void displayCommand(String command) {
        String string; // String more efficient than StringBuilder here
        string = terminalText + command + '_';
        terminal.setText(string);
        scrollDown();
    }

    private void executeCommand(String command) {
        command = command.substring(0, command.length() - 1);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(terminalText);
        stringBuilder.append(command);
        terminalText = stringBuilder.toString();
        terminal.setText(stringBuilder.toString());
        editText.setText("");
        execute(command);
        scrollDown();
    }

    public void execute(String command) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(terminalText);
        stringBuilder.append("\n\texecuting ");
        stringBuilder.append(command);
        stringBuilder.append("...\n");
        terminalText = stringBuilder.toString();
        terminal.setText(stringBuilder.toString());
        executeCallback();
        scrollDown();
    }

    public void executeCallback() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(terminalText);
        stringBuilder.append("\texecuted command\n");
        stringBuilder.append(this.getString(R.string.terminal));
        terminalText = stringBuilder.toString();
        stringBuilder.append('_');
        terminal.setText(stringBuilder.toString());
        scrollDown();
    }

    private void scrollDown() {
        scrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }, 100);
    }

}
