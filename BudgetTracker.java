import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

class Transaction {
    private String description;
    private double amount;
    private boolean isIncome;

    public Transaction(String description, double amount, boolean isIncome) {
        this.description = description;
        this.amount = amount;
        this.isIncome = isIncome;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public boolean isIncome() {
        return isIncome;
    }

    public String getType() {
        return isIncome ? "Income" : "Expense";
    }
}

public class BudgetTracker extends JFrame {
    private List<Transaction> transactions;
    private JTextField descriptionField, amountField;
    private JRadioButton incomeRadio, expenseRadio;
    private JTable transactionTable;
    private DefaultTableModel tableModel;
    private JLabel balanceLabel;

    public BudgetTracker() {
        transactions = new ArrayList<>();
        setTitle("Budget Tracker");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add Transaction"));

        inputPanel.add(new JLabel("Description:"));
        descriptionField = new JTextField();
        inputPanel.add(descriptionField);

        inputPanel.add(new JLabel("Amount:"));
        amountField = new JTextField();
        inputPanel.add(amountField);

        inputPanel.add(new JLabel("Type:"));
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        incomeRadio = new JRadioButton("Income");
        expenseRadio = new JRadioButton("Expense");
        ButtonGroup group = new ButtonGroup();
        group.add(incomeRadio);
        group.add(expenseRadio);
        radioPanel.add(incomeRadio);
        radioPanel.add(expenseRadio);
        inputPanel.add(radioPanel);

        JButton addButton = new JButton("Add Transaction");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTransaction();
            }
        });
        inputPanel.add(addButton);

        add(inputPanel, BorderLayout.NORTH);

        // Transaction Table
        String[] columnNames = {"Description", "Amount", "Type"};
        tableModel = new DefaultTableModel(columnNames, 0);
        transactionTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        add(scrollPane, BorderLayout.CENTER);

        // Balance Panel
        JPanel balancePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        balanceLabel = new JLabel("Balance: RS 0.00");
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        balancePanel.add(balanceLabel);
        add(balancePanel, BorderLayout.SOUTH);
    }

    private void addTransaction() {
        String description = descriptionField.getText();
        String amountText = amountField.getText();
        boolean isIncome = incomeRadio.isSelected();

        if (description.isEmpty() || amountText.isEmpty() || (!incomeRadio.isSelected() && !expenseRadio.isSelected())) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);
            Transaction transaction = new Transaction(description, amount, isIncome);
            transactions.add(transaction);
            updateTransactionTable();
            updateBalance();
            clearInputFields();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid amount", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTransactionTable() {
        tableModel.setRowCount(0);
        for (Transaction t : transactions) {
            Object[] row = {t.getDescription(), String.format("RS %.2f", t.getAmount()), t.getType()};
            tableModel.addRow(row);
        }
    }

    private void updateBalance() {
        double balance = 0;
        for (Transaction t : transactions) {
            if (t.isIncome()) {
                balance += t.getAmount();
            } else {
                balance -= t.getAmount();
            }
        }
        balanceLabel.setText(String.format("Balance: RS %.2f", balance));
    }

    private void clearInputFields() {
        descriptionField.setText("");
        amountField.setText("");
        incomeRadio.setSelected(false);
        expenseRadio.setSelected(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BudgetTracker().setVisible(true);
            }
        });
    }
}