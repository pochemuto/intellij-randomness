package com.fwdekker.randomness.integer;

import com.intellij.openapi.ui.ValidationInfo;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.fixture.Containers.showInFrame;


/**
 * GUI tests for {@link IntegerSettingsDialog}.
 */
public final class IntegerSettingsDialogTest extends AssertJSwingJUnitTestCase {
    private static final long DEFAULT_MIN_VALUE = 2147483883L;
    private static final long DEFAULT_MAX_VALUE = 6442451778L;
    private static final long DEFAULT_BASE = 10;

    private IntegerSettings integerSettings;
    private IntegerSettingsDialog integerSettingsDialog;
    private FrameFixture frame;


    @Override
    protected void onSetUp() {
        integerSettings = new IntegerSettings();
        integerSettings.setMinValue(DEFAULT_MIN_VALUE);
        integerSettings.setMaxValue(DEFAULT_MAX_VALUE);
        integerSettings.setBase((int) DEFAULT_BASE);

        integerSettingsDialog = GuiActionRunner.execute(() -> new IntegerSettingsDialog(integerSettings));
        frame = showInFrame(robot(), integerSettingsDialog.createCenterPanel());
    }


    @Test
    public void testDefaultIsValid() {
        final ValidationInfo validationInfo = GuiActionRunner.execute(() -> integerSettingsDialog.doValidate());

        assertThat(validationInfo).isNull();
    }


    @Test
    public void testLoadSettingsMinValue() {
        frame.spinner("minValue").requireValue(DEFAULT_MIN_VALUE);
    }

    @Test
    public void testLoadSettingsMaxValue() {
        frame.spinner("maxValue").requireValue(DEFAULT_MAX_VALUE);
    }

    @Test
    public void testLoadSettingsBase() {
        frame.spinner("base").requireValue(DEFAULT_BASE);
    }


    @Test
    public void testValidateMinValueFloat() {
        GuiActionRunner.execute(() -> frame.spinner("minValue").target().setValue(285.21f));

        frame.spinner("minValue").requireValue(285L);
    }

    @Test
    public void testValidateMaxValueFloat() {
        GuiActionRunner.execute(() -> frame.spinner("maxValue").target().setValue(490.34f));

        frame.spinner("maxValue").requireValue(490L);
    }

    @Test
    public void testValidateMaxValueGreaterThanMinValue() {
        GuiActionRunner.execute(() -> frame.spinner("maxValue").target().setValue(DEFAULT_MIN_VALUE - 1));

        final ValidationInfo validationInfo = integerSettingsDialog.doValidate();

        assertThat(validationInfo).isNotNull();
        assertThat(validationInfo.component).isEqualTo(frame.spinner("maxValue").target());
        assertThat(validationInfo.message).isEqualTo("The maximum should be no smaller than the minimum.");
    }

    @Test
    public void testValidateValueRange() {
        GuiActionRunner.execute(() -> {
            frame.spinner("minValue").target().setValue(Long.MIN_VALUE);
            frame.spinner("maxValue").target().setValue(Long.MAX_VALUE);
        });

        final ValidationInfo validationInfo = integerSettingsDialog.doValidate();

        assertThat(validationInfo).isNotNull();
        assertThat(validationInfo.component).isEqualTo(frame.spinner("maxValue").target());
        assertThat(validationInfo.message).isEqualTo("The range should not exceed 9.223372036854776E18.");
    }

    @Test
    public void testValidateBaseFloat() {
        GuiActionRunner.execute(() -> frame.spinner("base").target().setValue(22.62f));

        frame.spinner("base").requireValue(22L);
    }


    @Test
    public void testSaveSettingsWithoutParse() {
        GuiActionRunner.execute(() -> {
            frame.spinner("minValue").target().setValue((long) Integer.MAX_VALUE + 1L);
            frame.spinner("maxValue").target().setValue((long) Integer.MAX_VALUE + 2L);
            frame.spinner("base").target().setValue(14L);

            integerSettingsDialog.saveSettings();
        });

        assertThat(integerSettings.getMinValue()).isEqualTo(2147483648L);
        assertThat(integerSettings.getMaxValue()).isEqualTo(2147483649L);
        assertThat(integerSettings.getBase()).isEqualTo(14);
    }
}
