package org.urbanet.rtp.gui;

import java.io.IOException;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
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

    public MainWindow(Shell parentShell) {
        super(parentShell);
    }

    public void updateStatistics(final RtpStatistics stats) {
        if (!jitterLabel.isDisposed())
            jitterLabel.setText(JITTER_PREFIX + stats.getJitter());

        if (!packetsLostLabel.isDisposed())
            packetsLostLabel.setText(LOST_PACKETS_PREFIX
                    + stats.getPacketsLost());
    }

    protected Control createContents(Composite parent) {
        parent.setLayout(new RowLayout(SWT.VERTICAL));

        parent.addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent e) {
                try {
                    mediator.shutdown();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        Composite entryFields = new Composite(parent, SWT.NONE);
        entryFields.setLayout(new GridLayout(2, true));

        Label hostLabel = new Label(entryFields, SWT.LEFT);
        hostLabel.setText("Host:");

        final Text hostText = new Text(entryFields, SWT.SINGLE);
        hostText.setText("localhost");
        hostText.setTextLimit(100);

        Label portLabel = new Label(entryFields, SWT.LEFT);
        portLabel.setText("Port:");

        final Text portText = new Text(entryFields, SWT.SINGLE);
        portText.setText("8080");

        Label streamLabel = new Label(entryFields, SWT.LEFT);
        streamLabel.setText("Stream:");

        final Text streamText = new Text(entryFields, SWT.SINGLE);
        streamText.setText("/stream.sdp");

        Composite buttons = new Composite(parent, SWT.NONE);
        buttons.setLayout(new RowLayout(SWT.HORIZONTAL));

        startButton = new Button(buttons, SWT.PUSH);
        startButton.setText("Start");
        startButton.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                try {
                    mediator
                            .start(hostText.getText(), Integer
                                    .parseInt(portText.getText()), streamText
                                    .getText());
                } catch (NumberFormatException ex) {
                    mediator.exception(ex);
                }
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                this.widgetSelected(e);
            }
        });

        stopButton = new Button(buttons, SWT.PUSH);
        stopButton.setText("Stop");
        stopButton.addSelectionListener(new SelectionListener() {
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
        });

        Composite labels = new Composite(parent, SWT.NONE);
        labels.setLayout(new RowLayout(SWT.VERTICAL));

        jitterLabel = new Label(labels, SWT.LEFT);
        jitterLabel.setText(JITTER_PREFIX);

        packetsLostLabel = new Label(labels, SWT.LEFT);
        packetsLostLabel.setText(LOST_PACKETS_PREFIX);
        labels.pack();

        mediator = new ThreadMediator();
        mediator.setWindow(this);

        parent.pack();
        return parent;
    }
}