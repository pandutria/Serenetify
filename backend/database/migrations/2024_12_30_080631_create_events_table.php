<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        Schema::create('events', function (Blueprint $table) {
            $table->increments('id');
            $table->string('name', 255);
            $table->string('organizer', 255);
            $table->string('location', 255);
            $table->decimal('price', 10, 2)->default(0);
            $table->unsignedInteger('admin_id');
            $table->string('image_url')->nullable();
            $table->text('description')->nullable();
            $table->date('date');
            $table->time('time');
            $table->string('city', 100);
            $table->timestamps();

            $table->foreign('admin_id')->references('id')->on('users')->onDelete('cascade');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('events');
    }
};
