package payment.ui;

import payment.exception.PaymentException;
import payment.model.PaymentRequest;
import payment.model.PaymentResult;
import payment.model.PaymentStatus;
import payment.service.PaymentProcessor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Swing-based payment form UI.
 * Provides labeled fields, a method selector, and a result panel.
 */
public class SwingPaymentForm {

    private final PaymentProcessor paymentProcessor;

    private JFrame frame;
    private JComboBox<String> methodCombo;
    private JTextField amountField;
    private JTextField currencyField;
    private JLabel resultLabel;
    private JLabel statusIconLabel;

    public SwingPaymentForm(PaymentProcessor paymentProcessor) {
        this.paymentProcessor = paymentProcessor;
    }

    public void start() {
        SwingUtilities.invokeLater(this::buildAndShow);
    }

    private void buildAndShow() {
        frame = new JFrame("Odeme Ekrani");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(440, 330);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(Color.WHITE);

        // ── Header ──────────────────────────────────────────────────────────
        JPanel header = new JPanel();
        header.setBackground(new Color(0x1A73E8));
        header.setBorder(new EmptyBorder(16, 20, 16, 20));
        JLabel title = new JLabel("Odeme Formu");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        header.add(title);

        // ── Form ─────────────────────────────────────────────────────────────
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(20, 30, 10, 30));

        GridBagConstraints lc = new GridBagConstraints();
        lc.anchor = GridBagConstraints.WEST;
        lc.insets = new Insets(6, 0, 6, 12);
        lc.gridx = 0;

        GridBagConstraints fc = new GridBagConstraints();
        fc.fill = GridBagConstraints.HORIZONTAL;
        fc.weightx = 1.0;
        fc.insets = new Insets(6, 0, 6, 0);
        fc.gridx = 1;

        // Payment method
        lc.gridy = 0; fc.gridy = 0;
        form.add(label("Odeme Yontemi:"), lc);
        methodCombo = new JComboBox<>(new String[]{"creditcard", "paypal", "banktransfer"});
        styleCombo(methodCombo);
        form.add(methodCombo, fc);

        // Amount
        lc.gridy = 1; fc.gridy = 1;
        form.add(label("Tutar:"), lc);
        amountField = styledField("orn. 100.00");
        form.add(amountField, fc);

        // Currency
        lc.gridy = 2; fc.gridy = 2;
        form.add(label("Para Birimi:"), lc);
        currencyField = styledField("orn. TRY");
        form.add(currencyField, fc);

        // ── Pay button ───────────────────────────────────────────────────────
        JButton payBtn = new JButton("Odemeyi Gerceklestir");
        payBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        payBtn.setBackground(new Color(0x1A73E8));
        payBtn.setForeground(Color.WHITE);
        payBtn.setFocusPainted(false);
        payBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        payBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        payBtn.addActionListener(e -> handlePayment());

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setBorder(new EmptyBorder(0, 30, 10, 30));
        btnPanel.setLayout(new BorderLayout());
        btnPanel.add(payBtn, BorderLayout.CENTER);

        // ── Result panel ─────────────────────────────────────────────────────
        JPanel resultPanel = new JPanel(new BorderLayout(8, 0));
        resultPanel.setBackground(new Color(0xF8F9FA));
        resultPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(0xDEE2E6)),
                new EmptyBorder(12, 30, 12, 30)
        ));

        statusIconLabel = new JLabel("  ");
        statusIconLabel.setFont(new Font("SansSerif", Font.BOLD, 18));

        resultLabel = new JLabel("Sonuc burada gorunecek.");
        resultLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        resultLabel.setForeground(new Color(0x6C757D));

        resultPanel.add(statusIconLabel, BorderLayout.WEST);
        resultPanel.add(resultLabel, BorderLayout.CENTER);

        // ── Assemble ─────────────────────────────────────────────────────────
        root.add(header, BorderLayout.NORTH);
        root.add(form, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(Color.WHITE);
        bottom.add(btnPanel, BorderLayout.NORTH);
        bottom.add(resultPanel, BorderLayout.SOUTH);
        root.add(bottom, BorderLayout.SOUTH);

        frame.setContentPane(root);
        frame.setVisible(true);
    }

    private void handlePayment() {
        String method = (String) methodCombo.getSelectedItem();
        String amountText = amountField.getText().trim();
        String currency = currencyField.getText().trim();

        double amount;
        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            showResult(false, "Hata: Tutar sayisal olmali.");
            return;
        }

        try {
            PaymentRequest request = new PaymentRequest(amount, currency);
            PaymentResult result = paymentProcessor.process(method, request);
            boolean success = result.getStatus() == PaymentStatus.SUCCESS;
            showResult(success, result.getMessage());
        } catch (PaymentException e) {
            showResult(false, "Hata: " + e.getMessage());
        }
    }

    private void showResult(boolean success, String message) {
        if (success) {
            statusIconLabel.setText("✓");
            statusIconLabel.setForeground(new Color(0x28A745));
            resultLabel.setForeground(new Color(0x28A745));
        } else {
            statusIconLabel.setText("✗");
            statusIconLabel.setForeground(new Color(0xDC3545));
            resultLabel.setForeground(new Color(0xDC3545));
        }
        resultLabel.setText(message);
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.PLAIN, 13));
        l.setForeground(new Color(0x343A40));
        return l;
    }

    private JTextField styledField(String placeholder) {
        JTextField field = new JTextField(18);
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xCED4DA)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        field.setForeground(new Color(0xADB5BD));
        field.setText(placeholder);
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(new Color(0x343A40));
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setForeground(new Color(0xADB5BD));
                    field.setText(placeholder);
                }
            }
        });
        return field;
    }

    private void styleCombo(JComboBox<String> combo) {
        combo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        combo.setBackground(Color.WHITE);
    }
}
