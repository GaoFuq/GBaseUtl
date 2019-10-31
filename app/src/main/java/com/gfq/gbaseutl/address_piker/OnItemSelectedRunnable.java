package com.gfq.gbaseutl.address_piker;

final class OnItemSelectedRunnable implements Runnable {
    final AddressWheelView loopView;

    OnItemSelectedRunnable(AddressWheelView loopview) {
        loopView = loopview;
    }

    @Override
    public final void run() {
        loopView.onItemSelectedListener.onItemSelected(loopView.getCurrentItem());
    }
}
