/*
 * Copyright (c) 2015, Marjan Krsteski, Michael Madume and Neil Patrao
 * University of Southampton.
 * All rights reserved.
 *
 * Redistribution and use with or without modification,
 * are permitted provided that the following conditions are met:
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

import android.util.Log;

/**
 * Created by Neil on 11/3/2015.
 */

public class CircularStringBuffer {
    private String[] stringArray;
    private int currentPosition;
    private int bottomPosition;
    private int topPosition;
    private int cursor;
    private int length;
    private boolean reachedBottom;
    private boolean reachedTop;

    public CircularStringBuffer() {
        stringArray = new String[10];
        currentPosition=0;
        bottomPosition=0;
        topPosition=0;
        cursor=0;
        length = 10;
        reachedBottom = true;
        reachedTop = true;
    }

    public CircularStringBuffer(int l) {
        stringArray = new String[l];
        currentPosition=0;
        bottomPosition=0;
        topPosition=0;
        cursor=0;
        length = l;
        reachedBottom = true;
        reachedTop = true;
    }

    public void add(String string) {
        stringArray[currentPosition] = string;
        stringArray[currentPosition + 1] = "";
        currentPosition = (currentPosition + 1) % length;
        if (currentPosition == bottomPosition) {
            bottomPosition = (bottomPosition + 1) % length;
        }
        cursor = currentPosition;
        updateReachedTop();
        updateReachedBottom();
        Log.d("FTDI","Add: currentPosition = " + currentPosition + " bottomPosition = " + bottomPosition + " cursor = " + cursor
                + " reachedTop = " + reachedTop  + " reachedBottom = " + reachedBottom + " currentString = " + stringArray[cursor] );
    }

    public void addTemp(String string) {
        stringArray[currentPosition] = string;
        Log.d("FTDI","Add Temp: currentPosition = " + currentPosition + " bottomPosition = " + bottomPosition + " cursor = " + cursor
                + " reachedTop = " + reachedTop  + " reachedBottom = " + reachedBottom + " currentString = " + stringArray[cursor] );
    }

    public String getPrevious() {
        String previous = null;
        if (!reachedBottom) {
            cursor = (((cursor - 1) % length) + length ) % length;
            previous = stringArray[cursor];
            updateReachedTop();
            updateReachedBottom();
        }
        Log.d("FTDI","Prev: currentPosition = " + currentPosition + " bottomPosition = " + bottomPosition + " cursor = " + cursor
                + " reachedTop = " + reachedTop  + " reachedBottom = " + reachedBottom + " currentString = " + stringArray[cursor] );
        return previous;
    }

    public String getNext() {
        String next = null;
        if (!reachedTop) {
            cursor = (cursor + 1) % length;
            next = stringArray[cursor];
            updateReachedTop();
            updateReachedBottom();
        }
        Log.d("FTDI","Next: currentPosition = " + currentPosition + " bottomPosition = " + bottomPosition + " cursor = " + cursor
                + " reachedTop = " + reachedTop  + " reachedBottom = " + reachedBottom + " currentString = " + stringArray[cursor] );
        return next;
    }

    public void resetCursor() {
        cursor = currentPosition - 1;
    }

    public boolean isCursorAtTop() {
        return reachedTop;
    }

    public boolean isCursorAtBottom() {
        return reachedBottom;
    }

    private void updateReachedTop() {
        if (cursor == currentPosition) {
            reachedTop = true;
        } else {
            reachedTop = false;
        }
    }
    private void updateReachedBottom() {
        if (cursor == bottomPosition) {
            reachedBottom = true;
        } else {
            reachedBottom = false;
        }
    }
}
