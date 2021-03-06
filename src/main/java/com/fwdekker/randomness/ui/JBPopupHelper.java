package com.fwdekker.randomness.ui;

import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.popup.list.ListPopupImpl;
import com.intellij.ui.speedSearch.SpeedSearch;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.stream.IntStream;


/**
 * A collection of helper methods for dealing with {@link com.intellij.openapi.ui.popup.JBPopup}s.
 */
public final class JBPopupHelper {
    /**
     * Private constructor to prevent instantiation.
     */
    private JBPopupHelper() {
        // Do nothing
    }


    /**
     * Disables speed search.
     *
     * @param popup the popup to disable speed search for
     */
    public static void disableSpeedSearch(final ListPopupImpl popup) {
        final SpeedSearch speedSearch = popup.getSpeedSearch();
        speedSearch.setEnabled(false);
        speedSearch.addChangeListener(event -> speedSearch.updatePattern(""));
    }

    /**
     * Registers actions such that actions can be selected while holding {@code Shift}.
     *
     * @param popup       the popup to enable selecting with {@code Shift} for
     * @param normalTitle the title of the popup while the {@code Shift} key is not pressed
     * @param shiftTitle  the title of the popup while the {@code Shift} key is pressed
     */
    public static void registerShiftActions(final ListPopupImpl popup, final String normalTitle,
                                            final String shiftTitle) {
        popup.registerAction("shiftReleased", KeyStroke.getKeyStroke("released SHIFT"), new AbstractAction() {
            public void actionPerformed(final ActionEvent event) {
                popup.setCaption(normalTitle);
            }
        });
        popup.registerAction("shiftPressed", KeyStroke.getKeyStroke("shift pressed SHIFT"), new AbstractAction() {
            public void actionPerformed(final ActionEvent event) {
                popup.setCaption(shiftTitle);
            }
        });
        popup.registerAction("shiftInvokeAction", KeyStroke.getKeyStroke("shift ENTER"), new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                final KeyEvent keyEvent = new KeyEvent(popup.getComponent(), event.getID(), event.getWhen(),
                        event.getModifiers(), KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED,
                        KeyEvent.KEY_LOCATION_UNKNOWN);
                popup.handleSelect(true, keyEvent);
            }
        });

        final int nine = 9;
        IntStream.range(1, nine).forEach(key -> popup
                .registerAction("shiftInvoke" + key, KeyStroke.getKeyStroke("shift " + key),
                        new AbstractAction() {
                            @Override
                            public void actionPerformed(final ActionEvent event) {
                                final KeyEvent keyEvent = new KeyEvent(popup.getComponent(), event.getID(),
                                        event.getWhen(), event.getModifiers(),
                                        KeyEvent.VK_ENTER,
                                        KeyEvent.CHAR_UNDEFINED,
                                        KeyEvent.KEY_LOCATION_UNKNOWN);
                                popup.getList().addSelectionInterval(key - 1, key - 1);
                                popup.handleSelect(true, keyEvent);
                            }
                        }));
    }

    /**
     * Registers actions such that actions can be selected while holding {@code Ctrl} or {@code Cmd}.
     *
     * @param popup       the popup to enable selecting with {@code Ctrl}/{@code Cmd} for
     * @param normalTitle the title of the popup while the {@code Ctrl}/{@code Cmd} key is not pressed
     * @param ctrlTitle   the title of the popup while the {@code Ctrl}/{@code Cmd} key is pressed
     */
    public static void registerCtrlActions(final ListPopupImpl popup, final String normalTitle,
                                           final String ctrlTitle) {
        popup.registerAction("ctrlReleased", KeyStroke.getKeyStroke("released CONTROL"), new AbstractAction() {
            public void actionPerformed(final ActionEvent event) {
                popup.setCaption(normalTitle);
            }
        });
        popup.registerAction("ctrlPressed", KeyStroke.getKeyStroke("control pressed CONTROL"), new AbstractAction() {
            public void actionPerformed(final ActionEvent event) {
                popup.setCaption(ctrlTitle);
            }
        });
        popup.registerAction("ctrlInvokeAction", KeyStroke.getKeyStroke("control ENTER"), new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                final KeyEvent keyEvent = new KeyEvent(popup.getComponent(), event.getID(), event.getWhen(),
                        event.getModifiers(), KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED,
                        KeyEvent.KEY_LOCATION_UNKNOWN);
                popup.handleSelect(true, keyEvent);
            }
        });

        final int nine = 9;
        IntStream.range(1, nine).forEach(key -> popup
                .registerAction("ctrlInvoke" + key, KeyStroke.getKeyStroke("control " + key),
                        new AbstractAction() {
                            @Override
                            public void actionPerformed(final ActionEvent event) {
                                final KeyEvent keyEvent = new KeyEvent(popup.getComponent(), event.getID(),
                                        event.getWhen(), event.getModifiers(),
                                        KeyEvent.VK_ENTER,
                                        KeyEvent.CHAR_UNDEFINED,
                                        KeyEvent.KEY_LOCATION_UNKNOWN);
                                popup.getList().addSelectionInterval(key - 1, key - 1);
                                popup.handleSelect(true, keyEvent);
                            }
                        }));
    }

    /**
     * Displays a popup with a title and two messages.
     *
     * @param title    the title of the popup
     * @param messageA the first message
     * @param messageB the second message
     */
    public static void showMessagePopup(final String title, final String messageA, final String messageB) {
        final Runnable nop = () -> {
        };

        JBPopupFactory.getInstance().createConfirmation(
                title,
                messageA,
                messageB,
                nop,
                1
        ).showInFocusCenter();
    }
}
