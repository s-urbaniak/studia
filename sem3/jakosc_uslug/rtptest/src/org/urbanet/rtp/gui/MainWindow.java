package org.urbanet.rtp.gui;

import java.io.IOException;
import java.text.DecimalFormat;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.urbanet.rtp.protocol.beans.RtpStatistics;

public class MainWindow extends ApplicationWindow {
    private static final String LOST_PACKETS_PREFIX = "Lost packets: ";

    private static final String JITTER_PREFIX = "Jitter: ";

    private ThreadMediator mediator;

    private Button startButton, stopButton;

    private Label jitterLabel;

    private Label packetsLostLabel;

    DecimalFormat format;

    public MainWindow(Shell parentShell) {
        super(parentShell);
    }

    private final class DisposeWindowListener implements DisposeListener {
        @Override
        public void widgetDisposed(DisposeEvent e) {
            try {
                mediator.shutdown();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private final class StopListener implements SelectionListener {
        @Override
        public void widgetSelected(SelectionEvent e) {
            try {
                mediator.stop();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        @Override
        public void widgetDefaultSelected(SelectionEvent e) {
            this.widgetSelected(e);
        }
    }

    private final class StartListener implements SelectionListener {
        private final Text portText;
        private final Text streamText;
        private final Text hostText;

        private StartListener(Text portText, Text streamText, Text hostText) {
            this.portText = portText;
            this.streamText = streamText;
            this.hostText = hostText;
        }

        @Override
        public void widgetSelected(SelectionEvent e) {
            try {
                mediator.start(hostText.getText(), Integer.parseInt(portText
                        .getText()), streamText.getText());
            } catch (NumberFormatException ex) {
                mediator.exception(ex);
            }
        }

        @Override
        public void widgetDefaultSelected(SelectionEvent e) {
            this.widgetSelected(e);
        }
    }

    public void updateStatistics(final RtpStatistics stats) {
        if (!jitterLabel.isDisposed())
            jitterLabel.setText(JITTER_PREFIX
                    + format.format(stats.getJitter()));

        if (!packetsLostLabel.isDisposed())
            packetsLostLabel.setText(LOST_PACKETS_PREFIX
                    + stats.getPacketsLost());
    }

    protected Control createContents(Composite parent) {
        parent.addDisposeListener(new DisposeWindowListener());
        Canvas composite = new Canvas(parent, SWT.NONE);

        GridLayout parentLayout = new GridLayout(2, false);
        composite.setLayout(parentLayout);

        Label hostLabel = new Label(composite, SWT.NONE);
        hostLabel.setText("Host:");
        final Text hostText = new Text(composite, SWT.BORDER);
        GridData layoutData = new GridData();
        layoutData.horizontalAlignment = SWT.BEGINNING;
        layoutData.grabExcessHorizontalSpace = true;
        layoutData.minimumWidth = 150;
        hostText.setText("localhost");
        hostText.setLayoutData(layoutData);

        Label portLabel = new Label(composite, SWT.NONE);
        portLabel.setText("Port:");
        layoutData = new GridData();
        layoutData.horizontalAlignment = SWT.BEGINNING;
        layoutData.grabExcessHorizontalSpace = true;
        layoutData.minimumWidth = 75;
        final Text portText = new Text(composite, SWT.BORDER);
        portText.setLayoutData(layoutData);
        portText.setText("8080");

        Label streamLabel = new Label(composite, SWT.NONE);
        streamLabel.setText("Stream:");
        final Text streamText = new Text(composite, SWT.BORDER);
        layoutData = new GridData();
        layoutData.horizontalAlignment = SWT.BEGINNING;
        layoutData.grabExcessHorizontalSpace = true;
        layoutData.minimumWidth = 150;
        streamText.setLayoutData(layoutData);
        streamText.setText("/stream.sdp");

        startButton = new Button(composite, SWT.PUSH);
        startButton.setText("Start");
        startButton.addSelectionListener(new StartListener(portText,
                streamText, hostText));
        layoutData = new GridData(GridData.FILL, GridData.FILL, true, true);
        layoutData.horizontalSpan = 3;
        startButton.setLayoutData(layoutData);

        stopButton = new Button(composite, SWT.PUSH);
        stopButton.setText("Stop");
        stopButton.addSelectionListener(new StopListener());
        layoutData = new GridData(GridData.FILL, GridData.FILL, true, true);
        layoutData.horizontalSpan = 3;
        stopButton.setLayoutData(layoutData);

        jitterLabel = new Label(composite, SWT.NONE);
        jitterLabel.setText(JITTER_PREFIX);
        layoutData = new GridData(GridData.FILL, GridData.FILL, true, true);
        layoutData.horizontalSpan = 3;
        jitterLabel.setLayoutData(layoutData);

        format = new DecimalFormat("0.000");

        packetsLostLabel = new Label(composite, SWT.NONE);
        packetsLostLabel.setText(LOST_PACKETS_PREFIX);
        layoutData = new GridData(GridData.FILL, GridData.FILL, true, true);
        layoutData.horizontalSpan = 3;
        packetsLostLabel.setLayoutData(layoutData);

        mediator = new ThreadMediator();
        mediator.setWindow(this);

        return composite;
    }
}