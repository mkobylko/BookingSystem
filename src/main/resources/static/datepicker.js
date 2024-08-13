$(document).ready(function () {
    $('.input-daterange').datepicker({
        format: 'dd-mm-yyyy',
        autoclose: true,
        clearBtn: true,
        disableTouchKeyboard: true,
        calendarWeeks: false // Відключаємо відображення номерів тижнів
    });
});


function calculatePrice() {
    var startDate = $('#start').datepicker('getDate');
    var endDate = $('#end').datepicker('getDate');

    // Розрахунок кількості днів
    var timeDiff = Math.abs(endDate.getTime() - startDate.getTime());
    var numberOfNights = Math.ceil(timeDiff / (1000 * 3600 * 24));

    // Отримання ціни за ніч з прихованого поля
    var pricePerNight = parseInt($('input[name="price"]').val());

    // Розрахунок загальної ціни
    var totalPrice = numberOfNights * pricePerNight;

    // Виведення результатів
    $('#numberOfNights').text(numberOfNights);
    $('#totalPrice').text(totalPrice);
    $('#priceInfo').show();
}
