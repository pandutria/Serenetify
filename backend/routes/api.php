<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\AuthController;
use App\Http\Controllers\ChatController;
use App\Http\Controllers\EventController;
use App\Http\Controllers\ChatEventController;
use App\Http\Controllers\DoctorController;
use App\Http\Controllers\DoctorChatController;
use App\Http\Controllers\CommunityController;
use App\Http\Controllers\CommunityMessageController;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider and all of them will
| be assigned to the "api" middleware group. Make something great!
|
*/

Route::prefix('auth')->group(function () {
    Route::post('/register', [AuthController::class, 'register']);
    Route::post('/login', [AuthController::class, 'login']);
    Route::get('/users', [AuthController::class, 'getUsers']);
});

Route::prefix('chat')->group(function () {
    Route::post('/send', [ChatController::class, 'sendMessage']);
    Route::get('/{receiverId}', [ChatController::class, 'getChats']);
    Route::get('/chat-list', [ChatController::class, 'getChatList']);

});

Route::prefix('events')->group(function () {
    Route::post('/create', [EventController::class, 'createEvent']);
    Route::get('/', [EventController::class, 'getEvents']);
    Route::get('/{id}', [EventController::class, 'getEvent']);
    Route::delete('/{id}', [EventController::class, 'deleteEvent']);
});

Route::prefix('chat-events')->group(function () {
    Route::post('/send', [ChatEventController::class, 'sendMessage']);
    Route::get('/{receiverId}', [ChatEventController::class, 'getChats']);
    Route::get('/chat-list', [ChatEventController::class, 'getChatList']);
});

Route::post('/doctors', [DoctorController::class, 'createDoctor']);
Route::get('/doctors', [DoctorController::class, 'getDoctors']);
Route::get('/doctors/{id}', [DoctorController::class, 'getDoctor']);
Route::put('/doctors/{id}', [DoctorController::class, 'updateDoctor']);
Route::delete('/doctors/{id}', [DoctorController::class, 'deleteDoctor']);

Route::prefix('doctor-chat')->group(function () {
    Route::post('/send', [DoctorChatController::class, 'sendMessage']);
    Route::get('/{receiverId}', [DoctorChatController::class, 'getChats']);
    Route::get('/chat-list', [DoctorChatController::class, 'getChatList']);
});

Route::prefix('communities')->group(function () {
    Route::get('/', [CommunityController::class, 'getCommunities']);
    Route::get('/{id}', [CommunityController::class, 'getCommunity']);
    Route::post('/', [CommunityController::class, 'createCommunity']);
    Route::put('/{id}', [CommunityController::class, 'updateCommunity']);
    Route::delete('/{id}', [CommunityController::class, 'deleteCommunity']);
});

Route::prefix('community-messages')->group(function () {
    Route::get('/{communityId}', [CommunityMessageController::class, 'getMessages']);
    Route::post('/', [CommunityMessageController::class, 'createMessage']);
});

