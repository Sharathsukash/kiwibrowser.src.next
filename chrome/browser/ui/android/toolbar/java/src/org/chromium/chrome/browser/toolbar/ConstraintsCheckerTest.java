// Copyright 2022 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.chromium.chrome.browser.toolbar;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.annotation.Config;

import org.chromium.base.supplier.ObservableSupplierImpl;
import org.chromium.cc.input.BrowserControlsState;
import org.chromium.testing.local.LocalRobolectricTestRunner;
import org.chromium.ui.resources.dynamics.ViewResourceAdapter;

/** Unit tests for {@link ConstraintsChecker}. */
@RunWith(LocalRobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public final class ConstraintsCheckerTest {
    @Rule
    public MockitoRule mMockitoRule = MockitoJUnit.rule();

    @Mock
    private ViewResourceAdapter mViewResourceAdapter;

    private ObservableSupplierImpl mConstraintsSupplier = new ObservableSupplierImpl();

    @Test
    public void testScheduleRequestResourceOnUnlock() {
        ConstraintsChecker constraintsChecker =
                new ConstraintsChecker(mViewResourceAdapter, mConstraintsSupplier);
        constraintsChecker.scheduleRequestResourceOnUnlock();
        mConstraintsSupplier.set(BrowserControlsState.SHOWN);
        verify(mViewResourceAdapter, times(0)).onResourceRequested();

        mConstraintsSupplier.set(null);
        verify(mViewResourceAdapter, times(0)).onResourceRequested();

        mConstraintsSupplier.set(BrowserControlsState.BOTH);
        verify(mViewResourceAdapter, times(1)).onResourceRequested();
    }

    @Test
    public void testAreControlsLocked() {
        ConstraintsChecker constraintsChecker =
                new ConstraintsChecker(mViewResourceAdapter, mConstraintsSupplier);
        assertEquals(true, constraintsChecker.areControlsLocked());

        mConstraintsSupplier.set(BrowserControlsState.SHOWN);
        assertEquals(true, constraintsChecker.areControlsLocked());

        mConstraintsSupplier.set(BrowserControlsState.BOTH);
        assertEquals(false, constraintsChecker.areControlsLocked());
    }
}